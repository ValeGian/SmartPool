#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#include "contiki.h"
#include "sys/etimer.h"
#include "dev/leds.h"

#include "node-id.h"
#include "net/ipv6/simple-udp.h"
#include "net/ipv6/uip.h"
#include "net/ipv6/uip-ds6.h"
#include "net/ipv6/uip-debug.h"
#include "routing/routing.h"

#include "coap-engine.h"
#include "coap-blocking-api.h"

#define SERVER_EP "coap://[fd00::1]:5683"
#define CONNECTION_TRY_INTERVAL 1
#define REGISTRATION_TRY_INTERVAL 1
#define SIMULATION_INTERVAL 1
#define SENSOR_TYPE "presence_sensor"

/* Log configuration */
#include "sys/log.h"
#define LOG_MODULE "App"
#define LOG_LEVEL LOG_LEVEL_APP


PROCESS(presence_server, "Server for presence sensor");
AUTOSTART_PROCESSES(&presence_server);

char *service_url = "/registration";

static bool connected = false;
static bool registered = false;

static struct etimer wait_connectivity;
static struct etimer wait_registration;
static struct etimer simulation;

extern coap_resource_t presence_sensor; 

static void check_connection(){

  if(!NETSTACK_ROUTING.node_is_reachable()){

    LOG_INFO("Border Router not reachable\n");
    etimer_reset(&wait_connectivity);

  }else{

    LOG_INFO("Now the Border Router is reachable\n");
    leds_set(LEDS_NUM_TO_MASK(LEDS_YELLOW));
    connected = true;
  }
}


void client_chunk_handler(coap_message_t *response)
{
	const uint8_t *chunk;
	if(response == NULL) {
		LOG_INFO("Request timed out\n");
		etimer_set(&wait_registration, CLOCK_SECOND* REGISTRATION_TRY_INTERVAL);
		return;
	}
	
	int len = coap_get_payload(response, &chunk);
	printf("|%.*s", len, (char *)chunk);

	if(strncmp((char*)chunk, "Registration Completed!", len) == 0){ 
		leds_set(LEDS_NUM_TO_MASK(LEDS_GREEN));
		registered = true;
	}else
		etimer_set(&wait_registration, CLOCK_SECOND* REGISTRATION_TRY_INTERVAL);
}


PROCESS_THREAD(presence_server, ev, data)
{

	static coap_endpoint_t server_ep;
	static coap_message_t request[1]; /* This way the packet can be treated as pointer as usual. */			

	PROCESS_BEGIN();

	leds_on(LEDS_NUM_TO_MASK(LEDS_RED));
  etimer_set(&wait_connectivity, CLOCK_SECOND* CONNECTION_TRY_INTERVAL);
  
  while(!connected){
    PROCESS_WAIT_UNTIL(etimer_expired(&wait_connectivity));
    check_connection();
  }
	
	LOG_INFO("I'm connected!\n");
	
	while(!registered){
		
		LOG_INFO("[PRESENCE] sending registration message\n");	

		coap_endpoint_parse(SERVER_EP, strlen(SERVER_EP), &server_ep);	
		// Prepare the message
		coap_init_message(request, COAP_TYPE_CON,	COAP_POST, 0);
		coap_set_header_uri_path(request, service_url);
			
		coap_set_payload(request, (uint8_t *)SENSOR_TYPE, sizeof(SENSOR_TYPE) - 1);
		COAP_BLOCKING_REQUEST(&server_ep, request, client_chunk_handler);
		
    PROCESS_WAIT_UNTIL(etimer_expired(&wait_registration));
  }
	
	LOG_INFO("I'm registered!\n");
		
  LOG_INFO("Starting presence Server\n");

  coap_activate_resource(&presence_sensor, "presence_sensor");
  
  etimer_set(&simulation, CLOCK_SECOND * SIMULATION_INTERVAL);
  
  LOG_INFO("Loop\n");

  while(1) {
    PROCESS_WAIT_EVENT();
      
		if(ev == PROCESS_EVENT_TIMER && data == &simulation){		  
			presence_sensor.trigger();
			etimer_set(&simulation, CLOCK_SECOND * SIMULATION_INTERVAL);
		}
  }                             

  PROCESS_END();
}
