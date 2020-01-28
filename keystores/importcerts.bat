@ECHO OFF
ECHO Sad ce Laza da vam importuje sve nista se ne brinite. Pazi sad dobro bato!
keytool -import -noprompt -trustcacerts -alias sandbox_paypal -file sep/sandbox_paypal.cer -keystore cacerts -storepass changeit
PAUSE