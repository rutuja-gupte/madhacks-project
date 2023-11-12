![[Pasted image 20231112103931.png]]

# Welcome to Building Navigator!
### _The premier building guide!__

Our application uses the A* algorithm, image processing, and 24 hours of non-stop effort to bring to you what could be your **go-to** building navigation across campuses.

How to run the website:
Type "python local_app.py" in your terminal; <YOUR_IP_ADDRESS>:5000 should be the link to our website
Living on campus for about a year now, we realised that some buildings, such as the Humanities building and the Service Memorial Institute, can be very confusing for newcomers and sometimes even people who've been around for a while to navigate.
As our first project, we decided to come up with a little tool that can you help you go from room to room within a particular building based on the path from the A* algorithm which we applied to a preprocessed image of the building's floor plans.

We found the University buildings' floor plans from the library archives and university websites, and used pytesseract to extract the labels of the rooms along with their coordinates.
We then built a database storing the dataframes of these buildings with their rooms and their respective coordinates.
We then preprocessed the images of the floorplans using Opencv making contours so that the A* algorithm can run on the image more efficiently.
Based on the user's inputs, we select the dataframe and feed the algorithm the image, and the coordinates of the rooms.

The A* algorithm was the one implemented on the basis of the image that was passed in by the frontend. The command line arguments are then processed through by the GrayScalPicturee method of the stdlib-package.jar file. This image then gets an array of arrays of pixels that is either black or white. The code at every moment after every heuristic calculation checks whether the next node is valid as well as if it is unblocked and then finally gets the shortest path for every node. 

Notes:
1) We have a framework sort of ready to navigate between floors, but were not able to get it completely ready within the time limit.
2) We have a system in place to be able to add floor plans you'd like to be added to the database through the website, but again, were not able to fully implement within the time limit.
3) Our system however, would only work for floor plans with specific conditions, which our program follows by:
    - Room numbers must be specified. (We are working on working alphabetical name as well)
    - The stairs in the floor plan must be labeled "STAIRS".
    - The text on the floor plans should be crisp. 
4) Our UI is pretty weak right now, which we of course plan to improve.
5) We tried to print out the navigations for the complete path which as of now is still what we are working on and have not been able to implement it completely perfect and hope to improve it later on.

![[Pasted image 20231112104040.png]]

![[Pasted image 20231112104020.png]]
![[Pasted image 20231112103931.png]]
