# My Personal Project

## Project Description
An aim training program that will have targets appear, and the user is supposed to "shoot" the targets . 
The program will record every session, analyze the shots taken by comparing the position
of the shots taken to the position of the target, and give aim adjustment advice to the user after every session. 
Statistics from each session, such as headshot percentage, body shot percentage, leg shot 
percentage, and the final aim adjustment advice generated by all the shots taken in each session will be recorded and 
available for perusal in the future.

## Predicted Users and Ideation
This program will help **individuals who play first-person shooter games** improve their aim. This project is of interest to
me, because I play first-person shooter games such as Valorant. Though aim training systems do exist in the market
already, I have found that the programs just expect you to blindly shoot at targets instead of offering feedback based 
off of your performance while using the program. Since the shots taken during aim training sessions should be pretty 
indicative of the user's shooting habits, it would be a waste to not use all this valuable data to help improve the 
user's aim. Therefore, I wanted to create an aim training program that offers live feedback for the user's performance. 
Users will also be able to see their progression in aiming, since they would be able to access past sessions' 
statistics.
 

## User stories
As a user, I want to be able to :
- choose a point where my bullet is going (for console, it could be typed coordinates, for
the visualized program, it could be where the mouse taps on the interface) and have it be recorded
- adjust the distance between targets and my avatar
- analyze past shots in current session and get aim adjustment feedback (for example, "aim down" if I tend to 
shoot above my target)
- start a new training session and add it to the record of all training sessions
- view a list of every session and its relating statistics (for example, session 1 could include feedback for every shot and have a summary
 final aim adjustment feedback being "aim higher")
- add multiple (all) shots taken to a list of shots taken by the user in every session
- be able to view a list of all shots and suggestions given during the current session, as well as the final accuracy and summary 
suggestion given
- be able to have the option to save my current session to file
- be able to have the option to reload the file of a previous session and continue that session
- be able to filter out sessions that have an accuracy of over a certain inputted threshold

## Instructions for Grader 
- You can generate the first required action related to the user story "adding multiple shots to a session" by clicking
your mouse on the grey game panel to shoot shots. It will be automatically recorded.
- You can generate the second required action related to the user story "adding multiple sessions to an aim training 
system" by filtering out sessions where your accuracy is lower than a certain inputted threshold. You can access this 
feature by navigating to the menu bar on the AimTrainer window, clicking "View", and then "View filtered sessions". From 
there, you can use the scroll wheel to choose the accuracy threshold. Another action related to the user story "adding 
multiple shots to a session" is that you can view a filtered list of your perfect shots of the current session by 
navigating to the menu bar of the aimtrainer window, clicking on "View", then "View perfect shots for this session".
- You can locate my visual component by looking at the gray game panel. There should be a target that you 
should aim at. This target will move once you hit it.
- You can save the state of my application by going to the menu bar and clicking File, then clicking Save in the drop
down menu.
- You can reload the state of my application by selecting "Old Session" and then selecting the desired session number 
after using the scroll box. 
- Please also keep in mind when clicking on the aimtrainer that the program some times will not register the user's 
clicks: this is not because of a coding error, but because of java swing's mouse listener limitations. You can test this
claim by clicking on the exact same place if it did not register the first time (after thorough testing, I have found 
that if you are interval between two clicks is too short or if the click duration was too short, the program might not 
register it as a click)