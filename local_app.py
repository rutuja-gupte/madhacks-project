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
    query_str = f'select * from big_table where building == "{table}"'
    df = pd.read_sql(query_str, conn)
    df = df.set_index('text')
    
    options = []
    for row in df:
        options.append(df.index)
    option_html = [f"<option value='{option}'>{option}</option>" for option in options]
    rep = "".join(option_html)
     
        
    if flask.request.method == 'POST':
        src = flask.request.form['src']
        dst = flask.request.form['dst']
        
        src_floor = -1
        dst_floor = -1
        stair = ""
        
        src_floor = df.loc[src]['floor']
        dst_floor = df.loc[dst]['floor']
        
        return src_floor + dst_floor
#         for row, idx in df.iterrows():
#             if src == df.loc[row]["text"]:
#                 src_floor = df.loc[row]["floor"]
#             if dst == df.loc[row]["text"]:
#                 dst_floor = df.loc[row]["floor"]
        
#         if dst_floor == -1 or src_floor == -1:
#             # error
        
#         if src_floor - dst_floor > 0:
#             stair = "DN"
#         elif dst_floor - src_floor > 0:
#             stair = "UP"
        
#         src1 = df.loc[src]['top']
#         src2 = df.loc[src]['left']
        
#         if stair != "":
#             dst1 = df.loc[stair]["top"]
#             dst2 = df.loc[stair]["left"] # gotta mention to the user how many stairs they gotta climb
            
# #             gotta change fname
            
#             path1 = subprocess.run(['java', '-cp', '.:AStar.jar:stdlib-package.jar', 'Asterist.AStarGraph', fname, src1, src2, dst1, dst2], check=True, stdout=subprocess.PIPE).stdout
            
#             src1 = df.loc[stair]["top"]
#             src2 = df.loc[stair]["left"]
#             dst1 = df.loc[dst]["top"]
#             dst2 = df.loc[dst]["left"]
            
# #             change fname again
            
#             path2 = subprocess.run(['java', '-cp', '.:AStar.jar:stdlib-package.jar', 'Asterist.AStarGraph', fname, src1, src2, dst1, dst2], check=True, stdout=subprocess.PIPE).stdout
            
#         else:
#             dst1 = df.loc[dst]['top']
#             dst2 = df.loc[dst]['left']
#             fname = table + ".jpg"

#             path = subprocess.run(['java', '-cp', '.:AStar.jar:stdlib-package.jar', 'Asterist.AStarGraph', fname, src1, src2, dst1, dst2], check=True, stdout=subprocess.PIPE).stdout
#         return return_string # gotta change this
        return "TODO"
    with open("map.html") as f:
        html = f.read()
    html = html.replace("BUTTON1", rep)
    html = html.replace("BUTTON2", rep)
    html = html.replace('????',table)
    return html

# @app.route('/floorplan_add.html', methods=["POST"])
# def add():
#     if flask.request.method == 'POST':
#         bldg_img = flask.request.form['image']
#         bldg_name = flask.request.form['text']

if __name__ == "__main__":
    app.run(host="0.0.0.0", debug=True, threaded=False)
