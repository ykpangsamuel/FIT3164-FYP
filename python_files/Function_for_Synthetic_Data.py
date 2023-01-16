import pandas as pd

from random import choice as ch
from random import sample as sa

suburb     = pd.read_csv('C:/Users/User/Desktop/suburb_details.csv')
university = pd.read_csv('C:/Users/User/Desktop/uni_sub_distances.csv')


suburb = suburb.rename(columns={'suburb': 'sub_name'})

uni       = university['uni_name'].unique().tolist()
distance  = ['near', 'far']              # [(0-3 km), (0-6 km)]
rental    = ['Low', 'Medium', 'High']
places    = [1, 2, 3]                    # [gym, bank, restaurant]
transport = ['yes', 'no']                # if transport is 'no', night should be 'no' too
night     = ['yes', 'no']

rows  = []
times = range(50)
top_K = 5

for i in times:
    test = [ch(uni), ch(distance), ch(rental), sa(places, len(places)), ch(transport), ch(night)]
    if test[-2] == transport[-1]: test[-1] = night[-1]  
    rows.append(test)


# Filtering
# ---------
def filter_alg(rows, top_K, suburb, university):
    for i in rows:
        # Uni
        data = university[university['uni_name'] == i[0]]
        
        # Distance
        if   i[1] == 'near': temp1 = data[data['distance (km)'] <= 3]
        elif i[1] == 'far' : temp1 = data[data['distance (km)'] <= 6]
            
        # Rental
        temp1 = suburb[suburb['sub_name'].isin(temp1['sub_name'])]
        temp2 = temp1[temp1['rental_score'] == i[2]]
        
        # Others
        trans_score  = temp2['stop_score'] + temp2['transport_score']
        if i[5] == 'yes': temp2 = temp2[temp2['night_transport'] == True]
        
        gym_score    = temp2['place_gym_score'] * i[3][0]
        bank_score   = temp2['place_bank_score'] * i[3][1]
        rest_score   = temp2['place_restaurant_score'] * i[3][2]
        place_score  = (gym_score + bank_score + rest_score) / sum(i[3])
        suburb_score = trans_score + place_score
        
        temp3 = temp2[['sub_name']].join(pd.DataFrame(suburb_score, columns=['suburb_score']))
        
        if len(temp3) < top_K:
            temp4 = data[['sub_name', 'distance (km)']].merge(suburb, on='sub_name')
            
            trans_score  = temp4['stop_score'] + temp4['transport_score']
            gym_score    = temp4['place_gym_score'] * i[3][0]
            bank_score   = temp4['place_bank_score'] * i[3][1]
            rest_score   = temp4['place_restaurant_score'] * i[3][2]
            place_score  = (gym_score + bank_score + rest_score) / sum(i[3])
            suburb_score = trans_score + place_score
            
            temp4['suburb_score'] = suburb_score
            temp4 = temp4[['sub_name', 'distance (km)', 'rental', 'suburb_score']]
            temp4 = temp4.sort_values(['distance (km)', 'rental', 'suburb_score'])
            temp4 = list(temp4['sub_name'])
        
            if len(temp3) == 0:
                temp3 = temp4[0:top_K]
            
            else:
                temp3 = temp3.sort_values('suburb_score', ascending=False)
                temp3 = list(temp3['sub_name'])
                
                for j in range(top_K - len(temp3)):
                    for k in temp4:
                        if k not in temp3:
                            temp3.append(k)
                            break
        else:
            temp3 = list(temp3['sub_name'])
            temp3 = temp3[0:top_K]
            
        i.append(temp3)


filter_alg(rows, top_K, suburb, university)

data = pd.DataFrame(rows, columns=['uni', 'distance', 'rental', 'places', 'transport', 'night_transport', 'answer'])
temp = pd.DataFrame(data['places'].tolist(), columns=['place_gym', 'place_bank', 'place_restaurant'])

for i in temp.columns[::-1]: data.insert(3, i, temp[i])
del data['places']

# data.to_csv('synthetic_data.csv', index=False)
