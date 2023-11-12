import flask # requires installation if not already installed - pip3 install flask
import time

import numpy as np
import pandas as pd
import matplotlib.pyplot as plt
import re
from PIL import Image 
import os
import sqlite3

import numpy as np 
from PIL import Image as im 

app = flask.Flask("my application")

def find_num(string):
    if len(re.findall(r'\d', string)) > 0:
          return True
    return False

conn = sqlite3.connect("database.db")

def query(string):
    return pd.read_sql(string, conn)

@app.route('/')
def home():
    with open("index.html") as f:
        html = f.read()
    return html

@app.route('/map.html')
def mapping():
    with open("map.html") as f:
        html = f.read()
        
    query_string = dict(flask.request.args)
    table = query_string['building']
    query_str = f'select * from {table}'
    df = query(query_str)
    if flask.request.method == 'POST':
        src = flask.request.form['src']
        dst = flask.request.form['dst']
        
    return html

if __name__ == "__main__":
    app.run(host="0.0.0.0", debug=True, threaded=False)