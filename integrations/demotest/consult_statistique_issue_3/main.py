import os,requests,random
from dotenv import load_dotenv

load_dotenv()
from us1_helpers.main import main as initialize,backend_host

monitor_host = os.getenv('MONITOR_HOST', 'http:/34.117.40.226/sitemonitoring')
consult_stats = monitor_host+ '/consult/'


if __name__ == '__main__':


    print("Initializing with scenario 1 ....")
    products_created_references = initialize()

    print("\nScenario 3 : consult statistics")

    for i in range(50):
        index = random.randint(0, 2)
        #print("Searching",products_created_references[index])
        res = requests.get(backend_host + "/search/reference/" + products_created_references[index])
        assert res.status_code == 200

    for reference in products_created_references:
        print(requests.get(consult_stats+reference).json())

