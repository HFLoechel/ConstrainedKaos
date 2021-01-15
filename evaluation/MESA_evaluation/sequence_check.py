import json
import requests
import dinopy as dp
from time import sleep

def run_requests(i, res_all, entry_list, c, payload, header, url):
    try:
        for j in range(i, len(entry_list)):
            payload['sequence'] = entry_list[i][0].decode()
            res = requests.post(url, data=json.dumps(payload), headers=header)
            res_all.append(res)
            c += 1
            if c%1000 == 0:
                print(c)
    except:
        print("Max retries, going to sleep for 100 sec.")
        print("j= " + str(j))
        sleep(100)
        run_requests(j, res_all, entry_list, c, payload, header, url)

url = 'http://137.248.121.201:5000/api/all'
header = {'content-type': 'application/json;charset=UTF-8'}
with open("mesa.json") as json_file:
    config = json.load(json_file)
f = dp.FastaReader("mcgr_codewords.fasta")
payload = config
payload['asHTML'] = False
payload["key"]
c = 0
res_all = list()
i = 0
entry_list = list(f.entries())
run_requests(i, res_all, entry_list, c, payload, header, url)
