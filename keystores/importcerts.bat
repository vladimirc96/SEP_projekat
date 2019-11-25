@ECHO OFF
ECHO Sad ce Laza da vam importuje sve nista se ne brinite. Pazi sad dobro bato!
keytool -import -noprompt -trustcacerts -alias sep_discovery -file sep/sep_discovery.crt -keystore cacerts -storepass changeit
keytool -import -noprompt -trustcacerts -alias sep_gateway -file sep/sep_gateway.crt -keystore cacerts -storepass changeit
keytool -import -noprompt -trustcacerts -alias sep_koncentrator -file sep/sep_koncentrator.crt -keystore cacerts -storepass changeit
keytool -import -noprompt -trustcacerts -alias sep_bankservice -file sep/sep_bankservice.crt -keystore cacerts -storepass changeit
keytool -import -noprompt -trustcacerts -alias sep_paypalservice -file sep/sep_paypalservice.crt -keystore cacerts -storepass changeit
keytool -import -noprompt -trustcacerts -alias sep_bitcoinservice -file sep/sep_bitcoinservice.crt -keystore cacerts -storepass changeit

PAUSE