@ECHO OFF
ECHO KUME SRECNA TI NOVA GODINA!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
keytool -import -noprompt -trustcacerts -alias bbf_sep_bank -file bbf_sep_bank.cer -keystore cacerts -storepass changeit
PAUSE