#include “contiki.h”
#include "coap-engine.h“
#include "coap-blocking-api.h"
#include "dev/leds.h"
// Server IP and resource path
#define SERVER_EP "coap://[127.0.0.1]:5683"

char *service_url = "/registration";
static int connected = 0;
/ Define a handler to handle the response from the server


void
client_chunk_handler(coap_message_t *response)
{
	const uint8_t *chunk;
	if(response == NULL) {
		printf("Request timed out");
		return;
	}
	
	int len = coap_get_payload(response, &chunk);
	printf("|%.*s", len, (char *)chunk);

	if(strncmp((char*)chunk, "Registration Completed!", len) == 0){ 
		leds_off(LEDS_ALL);
		connected = 1;
	}
}

PROCESS_THREAD(er_example_client, ev, data)
{
	static coap_endpoint_t server_ep;
	static coap_message_t request[1]; /* This waythe packet can be treated as pointer as usual. */
	PROCESS_BEGIN();


	coap_endpoint_parse(SERVER_EP, strlen(SERVER_EP), &server_ep);
	// Prepare the message
	coap_init_message(request, COAP_TYPE_CON,	COAP_POST, 0);
	coap_set_header_uri_path(request, service_url);
	// Set the payload (if needed)
	const char msg[] = "IP-hydrom";
	coap_set_payload(request, (uint8_t *)msg, sizeof(msg) - 1);


	COAP_BLOCKING_REQUEST(&server_ep, request, client_chunk_handler);
