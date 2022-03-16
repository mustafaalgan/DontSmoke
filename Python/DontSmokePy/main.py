import pyrebase
import pandas as pd
import xlsxwriter as xlsxwriter

firebaseConfig = {
    "apiKey": "AIzaSyC5thVgwG5cR0CrTcXz7XyGuf2FLpzBN1Y",
    "authDomain": "dontsmoke-68c8c.firebaseapp.com",
    "databaseURL": "https://dontsmoke-68c8c-default-rtdb.firebaseio.com",
    "projectId": "dontsmoke-68c8c",
    "storageBucket": "dontsmoke-68c8c.appspot.com",
    "messagingSenderId": "1087786549753",
    "appId": "1:1087786549753:web:5b4420bb46da6479717d28",
    "measurementId": "G-17Y60J9K1T"
}

firebase = pyrebase.initialize_app(firebaseConfig)

db = firebase.database()

users = db.get()
genelListe = []
id = 0
for user in users.each():
    # print(user.key())
    tut = user.key()
    veriler = db.child(tut).get()

    liste = []
    id = id + 1

    for veri in veriler.each():
        # print(veri.val())
        liste.append(veri.val())
        tut = veri.val()
        # print(tut)
    genelListe.append(liste)

print(id)
say = 0
while say < id:
    pd.DataFrame(genelListe[say])

    pdHali = pd.DataFrame(genelListe[say])
    # print(user.key())
    say += 1
    # pdHali.to_excel("veriler.xls")
    print(pdHali.head())


