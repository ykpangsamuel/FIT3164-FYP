import pickle
import pandas as pd
import numpy  as np

from sklearn.model_selection import train_test_split
from sklearn.metrics         import accuracy_score, roc_auc_score, recall_score, precision_score, confusion_matrix
from sklearn                 import tree, preprocessing

data = pd.read_csv('C:/Users/User/Desktop/synthetic_data.csv')

def get_suburbs(model, data):
    label = model.classes_
    proba = model.predict_proba(X_test)
    pred  = model.predict(X_test)

    outcome = np.take(label, proba.argsort(axis=1))
    outcome = np.flip(outcome[:, -5:], axis=1)        # Top 5 suburbs
    
    return pred, outcome, proba


le = preprocessing.LabelEncoder()

for i in ['uni', 'distance', 'rental', 'transport', 'night_transport']:
    le.fit(data[i])
    data[i] = le.transform(data[i])
#     transformed = data[i].drop_duplicates()
#     reverse = le.inverse_transform(transformed)
#     
#     code = pd.DataFrame({i: reverse, 'code': transformed})
#     code = code.sort_values(['code'])
#     code = code.reset_index(drop=True)
#     print(code)
#     print()


# Get university codes
# --------------------
   
# temp = pd.DataFrame({'uni': x, 'code': data['uni']})
# temp = temp.drop_duplicates()
# temp = temp.sort_values(['code'])
# temp = temp.reset_index(drop=True)


X_train, X_test, Y_train, Y_test = train_test_split(data.iloc[:, :-1], data.iloc[:, -1], test_size=0.2)

model = tree.DecisionTreeClassifier()
model = model.fit(X_train, Y_train)

pred, outcome, probabilities = get_suburbs(model, X_test)

# Testing
# -------
test_string = 'Classification scores'
print(test_string + '\n' + '-' * len(test_string))
print()
print('Confusion matrix:' + '\n', confusion_matrix(Y_test, pred))
print()
print('Accuracy :', accuracy_score(Y_test, pred))
print('Recall   :', recall_score(Y_test, pred, average='micro'))
print('Precision:', precision_score(Y_test, pred, average='micro'))
print('AUC      :', roc_auc_score(Y_test, probabilities, multi_class='ovr'))




# print(model.score(X_test, Y_test))



# Save model
# ----------
# pickle.dump(model, open('finalized_model.sav', 'wb'))


# Load model
# ----------
# loaded_model = pickle.load(open('finalized_model.sav', 'rb'))
# pred  = loaded_model.predict(X_test)       # to get the prediction (recommended suburb)
# score = loaded_model.score(X_test, Y_test) # to get prediction score
