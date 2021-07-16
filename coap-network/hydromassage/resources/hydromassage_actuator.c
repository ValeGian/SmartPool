#include <stdlib.h>
#include <string.h>
#include "coap-engine.h"
#include "dev/leds.h"
#include "sys/log.h"


#define LOG_MODULE "App"
#define LOG_LEVEL LOG_LEVEL_APP

static void put_handler(coap_message_t *request, coap_message_t *response, uint8_t *buffer, uint16_t preferred_size, int32_t *offset);

RESOURCE(hydromassage_actuator,
         "title=\"Hydromassage actuator\";rt=\"Control\"",
         NULL,
         NULL,
         put_handler,
         NULL);

static bool hydromassage_on = false;
static int power_hydromassage = 5;


static void
put_handler(coap_message_t *request, coap_message_t *response, uint8_t *buffer, uint16_t preferred_size, int32_t *offset)
{
	size_t len = 0;
  const uint8_t* payload = NULL;
  int success = 1;
	
  if((len = coap_get_payload(request, &payload))) {

		if(strncmp((char*)payload, "on", len) == 0){ 
			hydromassage_on = true;
			LOG_INFO("Hydromassage ON\n");
			leds_set(LEDS_NUM_TO_MASK(LEDS_GREEN));
		}
		else if(strncmp((char*)payload, "off", len) == 0){ 
			hydromassage_on = false;
			LOG_INFO("Hydromassage OFF\n");
			leds_set(LEDS_NUM_TO_MASK(LEDS_RED));
		}else{
			char * token = strtok((char*)payload, "=");
			token = strtok(NULL, "=");
			power_hydromassage = atoi(token);
			printf("Hydromassage power changed to %d\n", power_hydromassage);
		}

	}  

  if(!success) 
    coap_set_status_code(response, BAD_REQUEST_4_00);  
}


