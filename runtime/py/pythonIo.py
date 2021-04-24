# pythonIo.py called from RpiNotifier to send msgs via spontit api
from spontitApi import SpontitApi;

api = SpontitApi("john_cockerham6705","EAUPYSHJGD2MHMT68CKVLE43F1N9AEYXZ20RVKMICM5IRFHZF7EQ935IQLDMGUYCPF9KPI6SPD9CT5I908AWIP67P9B53JSAUQJM")
api.push("from testApi")