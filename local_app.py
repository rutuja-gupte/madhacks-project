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


import json
from collections import deque

import numpy as np 
from PIL import Image as im 

app = flask.Flask("my application")

def find_num(string):
    if len(re.findall(r'\d', string)) > 0:
          return True
    return False

def dist(start, target):
    return ((start[0]-target[0])^2 + (start[1]-target[1])^2)^0.5

def nextstep(arr2, curr):
    mins = (0,0)
    maxs = (arr2.shape)
    
    l = []
    
    # up
    if curr[0] - 1 >= mins[0]:
        if arr2[curr[0] - 1, curr[1]] == 255:
            l.append((curr[0] - 1, curr[1]))
    
    # down
    if curr[0] + 1 < maxs[0]:
        if arr2[curr[0] + 1, curr[1]] == 255:
            l.append((curr[0] + 1, curr[1]))
    
    # left
    if curr[1] - 1 >= mins[1]:
        if arr2[curr[0], curr[1] - 1] == 255:
            l.append((curr[0], curr[1] - 1))
    
    # right
    if curr[1] + 1 < maxs[1]:
        if arr2[curr[0], curr[1] + 1] == 255:
            l.append((curr[0], curr[1] + 1))
            
    return l

def astar(arr, start, target):
    todo = deque([(start, 0)])
    visited = {}
    visited[start] = None
    
    while len(todo) > 0:
        item = todo.popleft()
        curr = item[0]
        if curr == target:
            return backtrace(visited, start, target)
        for child in nextstep(arr, curr):
            if child not in visited:
                visited[child] = curr
                todo.append((child, item[1]+1))
        todo = deque(sorted(todo, key = lambda i: i[1]))
    
    return "not found"

def backtrace(d, start, dst):
    curr = dst
    path = (dst,)
    while d[curr] != None:
        path = (d[curr], ) + path
        curr = d[curr]
    return path

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

        src1 = str(int(df.loc[src]["top"])) 
        src2 = str(int(df.loc[src]["left"]))
        dst1 = str(int(df.loc[dst]["top"]))
        dst2 = str(int(df.loc[dst]["left"]))


# #             change fname again

#             path2 = subprocess.run(['java', '-cp', '.:AStar.jar:stdlib-package.jar', 'Asterist.AStarGraph', fname, src2, src1, dst2, dst1], check=True, stdout=subprocess.PIPE).stdout

#         else:
#             dst1 = df.loc[dst]['top']
#             dst2 = df.loc[dst]['left']
#             fname = table + ".jpg"

#         path = subprocess.run(['java', '-classpath', './', 'Asterist.AStarGraph', os.path.join('floorplans', table, fname), src1, src2, dst1, dst2], stdout=subprocess.PIPE).stdout

#         path = str(path, encoding='utf-8')
#         outs = path.split("\n")
#         paths = outs[0]
#         directions = outs[1]

######working
        img = plt.imread(os.path.join('floorplans', table, fname))
        paths = astar(img[:,:,0], (int(src1), int(src2)), (int(dst1), int(dst2)))
        with open("temp.json", "w") as f:
            json.dump(paths, f)
       #### end of working     
            
                  
#         with open("temp.txt", "w") as f:
#             f.write(path)

#         return return_string # gotta change this

####working
        with open('result.html') as f:
            data = f.read()
        data = data.replace('??', fname)
        data = data.replace('###', table)
#         data = data.replace('INSTRUCTIONS', paths)
#         data = data.replace('INSTRUCTIONS', directions)
        return data
    with open("map.html") as f:
        html = f.read()
    html = html.replace("BUTTONS1", rep)
    html = html.replace("BUTTONS2", rep)
    html = html.replace('????',table)

    return html

@app.route('/dashboard.png')
def dashboard():
    query_string = dict(flask.request.args)
    fname = query_string['fname']
    dirs = query_string['dir']
    print(os.path.join('floorplans', dirs, fname))
    img = plt.imread(os.path.join('floorplans', dirs, fname))
#     figure = plt.imshow(img)

#     with open("temp.txt") as f:
#         data = f.read()
#     l = re.findall('\(\d+,\d+\)', data)
#     l2 = [item[1:-1].split(',') for item in l]
#     l3 = [(int(item[0]), int(item[1])) for item in l2]

### Working code
    with open("temp.json") as f:
        l3 = json.load(f)    
    print(l3)
### Idk working code    


#     img_test_result = img.copy()

#     for x,y in l3:
#         img_test_result[x,y] = 127

### Working code
    figure = plt.figure()
    axes = figure.add_subplot(111)
### Idk working code    

#     for i in l3:
#         p = plt.Circle(i, 10, color='red')
#         axes.add_patch(p)
#     if len(l3) > 0:
#         p1 = plt.Circle(l3[0], 10, color='red')
#         p2 = plt.Circle(l3[-1], 10, color='red')
#     axes.add_patch(p1)
#     axes.add_patch(p2)

    img5 = np.zeros((img.shape[0], img.shape[1], 3))
    for x in range(img.shape[0]):
        for y in range(img.shape[1]):
            img5[x,y] = np.array([img[x,y,0], 0, 255])

    for x,y in l3:
        img5[x,y] = np.array([0, 0, 0])

#     img6 = im.fromarray(img5) 
#     img6.save("temp.png")

    axes.imshow(img5)

#     axes.imshow(img_test_result, cmap="viridis")

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
