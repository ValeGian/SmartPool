#include <stdio.h>
#include <stdlib.h>
#include "coap-engine.h"
#include <time.h>

static void get_presence_handler(coap_message_t *request, coap_message_t *response, uint8_t *buffer, uint16_t preferred_size, int32_t *offset);
static void presence_event_handler(void);

EVENT_RESOURCE(presence_sensor,
         "title=\"Presence sensor\";obs",
         get_presence_handler,
         NULL,
         NULL,
         NULL,
         presence_event_handler);

static bool presence_detected = false;


static bool simulate_sensor(){
  srand(time(NULL));
  return (rand()%15 == 0)?!presence_detected:presence_detected;  //  1/45 probability to change state
}


static void
get_presence_handler(coap_message_t *request, coap_message_t *response, uint8_t *buffer, uint16_t preferred_size, int32_t *offset)
{
  const char* message = NULL;
  
  if (presence_detected)
    message = "ON";
  else
    message = "OFF";
  
  coap_set_header_content_format(response, TEXT_PLAIN);
  coap_set_payload(response, message, strlen(message));
}


static void
presence_event_handler(void)
{
  int sensed_state = simulate_sensor();
  
  if (presence_detected != sensed_state){
    
    presence_detected = sensed_state;
    if (presence_detected)
     	printf("Person in the pool\n");
    else
    	printf("None in the pool\n");
    	
    coap_notify_observers(&presence_sensor);
  }
}


