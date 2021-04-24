# pythonIo.py called from RpiNotifier to send msgs via spontit api
#  3 args are required
#  [0] = name of this file
#  [1] = text of notification to be sent
#  [2] = string of options containing
#           push email phone 
from resource import SpontitResource
from spontitApi import SpontitApi
import sys
id = "john_cockerham6705"
key = "EAUPYSHJGD2MHMT68CKVLE43F1N9AEYXZ20RVKMICM5IRFHZF7EQ935IQLDMGUYCPF9KPI6SPD9CT5I908AWIP67P9B53JSAUQJM"
emails = ["jupjcc@icloud.com"]
phones = ["+15613392779"]
dbf = open(r"/home/pi/bin/logs/pydbgPrePush.log","w")

resp = ""
nargs = len(sys.argv);
dbug = "in python function with " + str(len(sys.argv)) + " parameters:\n"
for a in range(0, nargs):
    dbug += str(a) + ".  " + str(sys.argv[a]) + "\n"
if nargs > 1:
    api = SpontitApi(id, key)
    msg = sys.argv[1]
    # if "push" in sys.argv[2]:
    #     resp += str(api.push(msg)) + "\n"
    #     if "email" in sys.argv[2]:
    #         resp += str(api.push(
    #             push_content="push_content",
    #             content="content",
    #             push_to_emails=emails)) + "\n"
    #     if "phone" in sys.argv[2]:
    #         resp += str(api.push(
    #             push_content="push_content",
    #             content="content",
    #             push_to_phone_numbers=phones))
    if "push" in sys.argv[2]:
        dbug = "calling push with args:\n"
        dbug += "msg = <" + msg + ">\n"
        if "email" in sys.argv[2]:
            ems = emails
            dbug += "emails: "
            ne = len(emails)
            for e in range(0, ne):
                dbug += str(emails[e]) + "\n"
        else:
            ems = None
        if "phone" in sys.argv[2]:
            phs = phones
            dbug += "emails: "
            np = len(phones)
            for p in range(0, np):
                dbug += str(phones[p]) + "\n"
        else:
            phs = None
        dbug += "\n"
        dbf.write(dbug)
        dbf.close()
        resp = api.simple_push_to_specific_phone_numbers_and_emails_example(
                content = msg,
                phone_numbers = phs,
                emails = ems
        )
print("response: " + str(resp))    
