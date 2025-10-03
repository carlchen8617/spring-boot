set -o pipefail
sleep 5
ldapmodify -x -H ldap://localhost -D cn=admin,cn=config -w config -f /opt/code/ac.ldif &
sleep 5
ldapadd -H ldap://localhost -D cn=admin,dc=ccnab,dc=com -w 1234 -f /opt/code/add2.ldif &
