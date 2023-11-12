import flask # requires installation if not already installed - pip3 install flask
import time

import numpy as np
import pandas as pd
import matplotlib.pyplot as plt
import re
from PIL import Image 
from pytesseract import pytesseract 
import os

import numpy as np 
from PIL import Image as im 

app = flask.Flask("my application")

def find_num(string):
            if len(re.findall(r'\d', string)) > 0:
                return True
            return False


image_path = os.path.join("C:\\", "My Data", "Academics", "Madhacks23", "madhacks-project", "image.jpg")
path_to_tesseract = r"C:\Program Files\Tesseract-OCR\tesseract.exe"
img = Image.open(image_path, 'r') 
pytesseract.tesseract_cmd = path_to_tesseract 
data = pytesseract.image_to_data(image_path)
img.close()
weird_string = re.findall("([\s\S]*)\n$", data)
l = [line.split('\t') for line in weird_string[0].split('\n')]
df = pd.DataFrame(l[1:], columns = l[0])
filtered_df = df[(df['text'] != '') & (df['conf'].astype(float) > 95)]
final_df = filtered_df[filtered_df['text'].apply(find_num)][['left', 'top', 'text']]


@app.route('/')
def home():
    with open("index.html") as f:
        html = f.read()
    return html

@app.route('/map.html')
def mapping():
    query_string = dict(flask.request.args)
    if query_string['building'] == 'aghall':
        return "TODO"

if __name__ == "__main__":
    app.run(host="0.0.0.0", debug=True, threaded=False)