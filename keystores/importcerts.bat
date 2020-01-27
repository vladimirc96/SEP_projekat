@ECHO OFF
ECHO Sad ce Laza da vam importuje sve nista se ne brinite. Pazi sad dobro bato!
keytool -import -noprompt -trustcacerts -alias bbf_sep_nc -file sep/bbf_sep_nc.cer -keystore cacerts -storepass changeit
PAUSE