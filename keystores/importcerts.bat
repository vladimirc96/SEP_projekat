@ECHO OFF
ECHO Sad ce Laza da vam importuje sve nista se ne brinite. Pazi sad dobro bato!
keytool -import -noprompt -trustcacerts -alias bbf_sep_nc_jove -file sep/bbf_sep_nc_vule.cer -keystore cacerts -storepass changeit
keytool -import -noprompt -trustcacerts -alias bbf_sep_nc_milan -file sep/bbf_sep_nc_milan.cer -keystore cacerts -storepass changeit
PAUSE