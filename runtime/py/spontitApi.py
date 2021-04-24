from spontit import SpontitResource
# from examples import Examples

class SpontitApi:
    __resource = None
    __emails = []
    __phone_numbers = []
    
    def __init__(self, id, key):
        # print("SpontitApi init")
        self.__resource = SpontitResource(id, key)
        #assert type(resource) == SpontitResource

    def push(self, msg):
        response = self.__resource.push(msg)
        print("msg:" + msg)
        print("Response=" + str(response))

    def setTargets(self,
                   phone_numbers = [],
                   emails = []):
        __emails = emails
        __phones = phone_numbers

    def simple_push_to_specific_phone_numbers_and_emails_example(self,
                    content = "no msg",
                    phone_numbers = [],
                    emails = []):
        dbf = open(r"/home/pi/bin/logs/pydbgInPush.log","w")
        dbug = "in api pushpython function: content=<" + content + ">\n"
        if phone_numbers is not None:
            np = len(phone_numbers)
            dbug += "sending to " + str(np) + " phone #(s)\n"
            for p in range(0, np):
                dbug += str(p+1) + ". " + phone_numbers[p] + "\n"
        if emails is not None:
            ne = len(emails)
            dbug += "sending to " + str(ne) + " email(s)\n"
            for e in range(0, ne):
                dbug += str(e+1) + ". " + emails[e] + "\n"
        dbf.write(dbug)
        dbf.close()

        """
        Sends a push notification to phone numbers and emails. We link the user account to the phone numbers and emails
        defined, and then send a push via Spontit.
        The users linked **do not have to follow your account** to receive the push notification.
        They will have the option to follow you or report you for spam. If you are reported for spam multiple times,
        your account will be restricted.
        :return:
        """
        response = self.__resource.push(
            push_content = content,
            push_to_phone_numbers = phone_numbers,
            push_to_emails = emails
        )
        # rsp = "Notification sent to:\nEmails: " + {str(emails)}
        # rsp +=    "\nPhone numbers: " + {str(phone_numbers)}
        print(response)
        return response
# if __name__ == "__main__":
#     print("in __main__ branch")
#     # Try an example...
#     # Get your userId at spontit.com/profile
#     # Get your secretKey at spontit.com/secret_keys
#     spontit_src = SpontitResource("john_cockerham6705","EAUPYSHJGD2MHMT68CKVLE43F1N9AEYXZ20RVKMICM5IRFHZF7EQ935IQLDMGUYCPF9KPI6SPD9CT5I908AWIP67P9B53JSAUQJM")

#     example_instance = Examples(spontit_src)

#     push_response = example_instance.simple_push_example()
#     print("Simple push example result: " + str(push_response))

"""
    # ...or be bold...
    example_instance.do_everything()

    # ...or get right to pushing!
    push_response = spontit_src.push("Hello!!!")
    print("Result: " + str(push_response))

    # To see documentation, run:
    help(SpontitResource)
"""

