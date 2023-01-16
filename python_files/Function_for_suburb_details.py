import pandas   as pd
import requests as rq

from math       import floor, ceil
from ptv.client import PTVClient


key    = 'AIzaSyAUYgfpxrWvkF7rgvoAB7vL92uX7B1Zoa0'
client = PTVClient(3002206, 'a54d7e33-1313-4f06-a989-6f374a2e0b44')

def query_coord(suburb, key):
    param  = f'input={suburb},%20Australia&inputtype=textquery&fields=geometry&key={key}'
    url    = f'https://maps.googleapis.com/maps/api/place/findplacefromtext/json?{param}'
    
    data   = rq.get(url)
    data   = data.json()
    data   = data['candidates'][0]['geometry']['location']
    
    return (data['lat'], data['lng'])

def query_place(item, latitude, longitude, key):
    param  = f'location={latitude}%2C{longitude}&radius=5000&keyword={item}&key={key}'
    url    = f'https://maps.googleapis.com/maps/api/place/nearbysearch/json?{param}'
    
    data = rq.get(url)
    data = data.json()
    data = data['results']
    
    return len(data)


rental = pd.read_csv('C:/Users/User/Desktop/rental_rate_for_details.csv')
places = pd.read_csv('C:/Users/User/Desktop/places.csv')
trans  = pd.read_csv('C:/Users/User/Desktop/transport_ids.csv')

places  = places.values.flatten().tolist()

trans = trans['route_type'].str.lower()
trans = 'transport_' + trans.str.replace(' ', '_')
trans = trans.values.tolist()


# temp = pd.read_csv('C:/Users/User/Desktop/suburb_details.csv')
temp = temp.sort_values('suburb')

coord = [query_coord(i, key) for i in temp['suburb']]
temp['latitude'], temp['longitude'] = zip(* coord)


transport_type = []
transport_stop = []
stop_ids       = []

for i in temp['suburb']:
    result = client.search(i, match_stop_by_suburb=True, include_outlets=False)
    result = result['stops']
    
    ids    = [j['stop_id'] for j in result]
    result = [j['route_type'] for j in result]

    stops  = len(result)
    types  = [0] * 5
    
    if stops > 0:
        for i in set(result): types[i] = 1
    
    transport_type.append(types)
    transport_stop.append(stops)
    stop_ids.append(ids)

    
temp['stop_ids'] = stop_ids
temp['transport_stop'] = transport_stop
temp = temp.join(pd.DataFrame(transport_type, columns=trans))


def check_night_transport(stop_id):
    for j in range(4 + 1):
        temp = client.get_departures_from_stop(j, stop_id)
        temp = temp['departures']
        if temp == []: continue
        
        for k in temp:
            time = k['scheduled_departure_utc']
            time = time.split('T')
            time = time.pop()[:2]
            time = int(time)
            if 9 <= time < 17: return True
    
    return False


stop = temp['stop_ids']
stop = stop.str.replace(', ', '","', regex=False)
stop = stop.str.replace('[' , '["' , regex=False)
stop = stop.str.replace(']' , '"]' , regex=False)
stop = stop.apply(eval)
stop = stop.values
stop = [[int(j) for j in i if j is not ''] for i in stop]
temp['stop_ids'] = stop

night_trans = []

for i in stop:
    for j in i:
        result = check_night_transport(j)
        
        if result is True:
            night_trans.append(result)
            break
        
        else:
            continue
    else:
        night_trans.append(False)
        
temp['night_transport'] = night_trans



def round_down(number, figure):
    return int(floor(number / figure)) * figure

def round_up(number, figure):
    return int(ceil(number / figure)) * figure

rental_q1  = temp['rental'].quantile(1/3)
rental_q1  = round_down(rental_q1, 100)

rental_q2 = temp['rental'].quantile(2/3)
rental_q2 = round_up(rental_q2, 100)

stop_q1 = temp['transport_stop'].quantile(1/5)
stop_q2 = temp['transport_stop'].quantile(2/5)
stop_q3 = temp['transport_stop'].quantile(3/5)
stop_q4 = temp['transport_stop'].quantile(4/5)

def rental_score(item):
    global rental_q1, rental_q2
    
    if   item < rental_q1             : return 'Low'
    elif rental_q1 <= item < rental_q2: return 'Medium'
    elif rental_q2 <= item            : return 'High'
    

def stop_score(item):
    global stop_q1, stop_q2, stop_q3, stop_q4
    
    if              item < stop_q1: return 1
    elif stop_q1 <= item < stop_q2: return 2
    elif stop_q2 <= item < stop_q3: return 3
    elif stop_q3 <= item < stop_q4: return 4
    elif stop_q4 <= item          : return 5


def place_score(item):
    if   0  <= item < 5 : return 1
    elif 5  <= item < 10: return 2
    elif 10 <= item < 15: return 3
    elif 15 <= item < 20: return 4
    elif item >= 20     : return 5
    
    
temp['rental_score'] = temp['rental'].apply(rental_score)
temp['transport_score'] = temp[temp.columns[8:13]].sum(axis=1)
temp['stop_score'] = temp['transport_stop'].apply(stop_score)
temp[temp.columns[4:7] + '_score'] = temp[temp.columns[4:7]].applymap(place_score)

# temp.to_csv('suburb_details.csv', index=False)




