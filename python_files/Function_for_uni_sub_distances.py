import pandas as pd
import numpy  as np

from math import radians
from sklearn.metrics.pairwise import haversine_distances as hv_dist


def find_distance(uni, sub):
    '''
    Function : Calculate the distance in km between each university and distance
    Arguments: - uni = data on universities
               - sub = data on suburbs
    '''
    uni = uni[['name'  , 'latitude', 'longitude']].rename(columns={'name'  :'uni_name'})
    sub = sub[['suburb', 'latitude', 'longitude']].rename(columns={'suburb':'sub_name'})

    uni_rad = np.radians(uni[['latitude', 'longitude']])
    sub_rad = np.radians(sub[['latitude', 'longitude']])

    dist = hv_dist(uni_rad, sub_rad)
    dist = dist.flatten() * (6371000 / 1000)

    data = uni[['uni_name']].merge(sub['sub_name'], how='cross')
    data['distance (km)'] = dist
    data = data.sort_values(['uni_name', 'sub_name'])
    
    return data


# File reading
# ------------
uni  = pd.read_csv('C:/Users/User/Desktop/university_list.csv')
sub  = pd.read_csv('C:/Users/User/Desktop/suburb_details.csv')


# Driver code
# -----------
data = find_distance(uni, sub)
data.to_csv('uni_sub_distances.csv', index=False)
