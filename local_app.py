import flask 
import time

import numpy as np
import pandas as pd
import matplotlib.pyplot as plt
import re
from PIL import Image 
import os
import sqlite3
import subprocess

import numpy as np 
from PIL import Image as im 

app = flask.Flask("my application")

def find_num(string):
    if len(re.findall(r'\d', string)) > 0:
          return True
    return False


@app.route('/')
def home():
    with open("index.html") as f:
        html = f.read()
    return html


@app.route('/map.html', methods=["POST", "GET"])
def mapping():
    
    conn = sqlite3.connect("database.db")
        
    query_string = dict(flask.request.args)
    table = query_string['building']
    query_str = f'select * from {table}'
    df = pd.read_sql(query_str, conn)
    df = df.set_index('text')
    if flask.request.method == 'POST':
        src = flask.request.form['src']
        dst = flask.request.form['dst']
        src1 = df.loc[src]['top']
        src2 = df.loc[src]['left']
        
        dst1 = df.loc[dst]['top']
        dst2 = df.loc[dst]['left']
        fname = table + ".jpg"

        return_string = subprocess.run(['java', '-cp', '.:AStar.jar:stdlib-package.jar', 'Asterist.AStarGraph', fname, src1, src2, dst1, dst2], check=True, stdout=subprocess.PIPE).stdout
        return return_string
       
    with open("map.html") as f:
        html = f.read()
    
    return html

@app.route('/floorplan_add.html', methods=["POST"])
def add():
    if flask.request.method == 'POST':
        bldg_img = flask.request.form['image']
        bldg_name = flask.request.form['text']

if __name__ == "__main__":
    app.run(host="0.0.0.0", debug=True, threaded=False)
