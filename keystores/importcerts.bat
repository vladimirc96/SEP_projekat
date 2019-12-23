@ECHO OFF
ECHO Sad ce Laza da vam importuje sve nista se ne brinite. Pazi sad dobro bato!
keytool -import -noprompt -trustcacerts -alias bbf_root_ca -file sep/bbf_root_ca.cer -keystore cacerts -storepass changeit
keytool -import -noprompt -trustcacerts -alias bbf_sep_discovery -file sep/bbf_sep_discovery.cer -keystore cacerts -storepass changeit
keytool -import -noprompt -trustcacerts -alias bff_sep_api-gateway -file sep/bbf_sep_api_gateway.cer -keystore cacerts -storepass changeit
keytool -import -noprompt -trustcacerts -alias bff_sep_sellers -file sep/bbf_sep_sellers.cer -keystore cacerts -storepass changeit
keytool -import -noprompt -trustcacerts -alias bff_sep_bank-service -file sep/bbf_sep_bank_service.cer -keystore cacerts -storepass changeit
keytool -import -noprompt -trustcacerts -alias bff_sep_paypal-service -file sep/bbf_sep_paypal_service.cer -keystore cacerts -storepass changeit
keytool -import -noprompt -trustcacerts -alias bff_sep_bitcoin-service -file sep/bbf_sep_bitcoin_service.cer -keystore cacerts -storepass changeit
keytool -import -noprompt -trustcacerts -alias bff_sep_load-balancer -file sep/bbf_sep_load_balancer.cer -keystore cacerts -storepass changeit
PAUSE