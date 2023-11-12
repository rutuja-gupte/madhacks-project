import flask 
import time

import numpy as np
import pandas as pd
import matplotlib
matplotlib.use('Agg')
import matplotlib.pyplot as plt
import re
from PIL import Image 
import os
import sqlite3
import subprocess
import io

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
    
    options = list(df.index)
#     for row in df:
#         options.append(df.index)
    option_html = [f"<option value='{option}'>{option}</option>" for option in options]
    rep = "".join(option_html)
     
        
    if flask.request.method == 'POST':
        src = flask.request.form['src2']
        dst = flask.request.form['dst2']
        
#         src_floor = -1
#         dst_floor = -1
#         stair = ""
        
        src_floor = df.loc[src]['floor']
        dst_floor = df.loc[dst]['floor']
        
        if src_floor != dst_floor:
            return "Not on the same floor"
        
        fname = table+"_"+src_floor+".jpg"
        
        
#         return src_floor + dst_floor
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

        src1 = str(int(df.loc[src]["top"])+50) 
        src2 = str(int(df.loc[src]["left"])+50)
        dst1 = str(int(df.loc[dst]["top"])-50)
        dst2 = str(int(df.loc[dst]["left"])-50)
        

# #             change fname again

#             path2 = subprocess.run(['java', '-cp', '.:AStar.jar:stdlib-package.jar', 'Asterist.AStarGraph', fname, src2, src1, dst2, dst1], check=True, stdout=subprocess.PIPE).stdout

#         else:
#             dst1 = df.loc[dst]['top']
#             dst2 = df.loc[dst]['left']
#             fname = table + ".jpg"

        path = subprocess.run(['java', '-classpath', './', 'Asterist.AStarGraph', os.path.join('floorplans', table, fname), src1, src2, dst1, dst2], stdout=subprocess.PIPE).stdout
        
        path = str(path, encoding='utf-8')
        outs = path.split("\n")
        paths = outs[0]
        directions = outs[1]
        
        with open("temp.txt", "w") as f:
            f.write(paths)
    
#         return return_string # gotta change this
        with open('result.html') as f:
            data = f.read()
        data = data.replace('??', fname)
        data = data.replace('###', table)
#         data = data.replace('INSTRUCTIONS', path)
        data = data.replace('INSTRUCTIONS', directions)
        return data
    with open("map.html") as f:
        html = f.read()
    html = html.replace("BUTTONS1", rep)
    html = html.replace("BUTTONS2", rep)
    html = html.replace('????',table)

    return html

@app.route('/dashboard.svg')
def dashboard():
    query_string = dict(flask.request.args)
    fname = query_string['fname']
    dirs = query_string['dir']
    print(os.path.join('floorplans', dirs, fname))
    img = plt.imread(os.path.join('floorplans', dirs, fname))
#     figure = plt.imshow(img)

    with open("temp.txt") as f:
        data = f.read()
    l = re.findall('\(\d+,\d+\)', data)
    l2 = [item[1:-1].split(',') for item in l]
    l3 = [(int(item[0]), int(item[1])) for item in l2]
    
    
  
    img_test_result = img.copy()[:,:]

    for x,y in l3:
        img_test_result[x,y] = 127
        
    figure = plt.figure()
    axes = figure.add_subplot(111)
    for i in l3:
        p = plt.Circle(i, 10, color='red')
        axes.add_patch(p)
#     if len(l3) > 0:
#         p1 = plt.Circle(l3[0], 10, color='red')
#         p2 = plt.Circle(l3[-1], 10, color='red')
#     axes.add_patch(p1)
#     axes.add_patch(p2)
    axes.imshow(img_test_result, cmap="viridis")

#     axes.imshow(img_test_result)
#     return the plot by saving local copy
    with open("temp.png", "wb") as f:
        figure.savefig(f)
    with open("temp.png", "rb") as f:
        return flask.Response(f.read(), headers = {"Content-Type":"image/png"})

# @app.route('/floorplan_add.html', methods=["POST"])
# def add():
#     if flask.request.method == 'POST':
#         bldg_img = flask.request.form['image']
#         bldg_name = flask.request.form['text']

if __name__ == "__main__":
    app.run(host="0.0.0.0", debug=True, threaded=False)
