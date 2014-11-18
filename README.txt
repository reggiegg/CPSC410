HOW TO SET UP AND RUN OUR PROJECT
=================================

!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
!                                     !
! PRECONDITIONS:                      !
! java and subversion must be         !
! installed.                          !
!                                     !
! Also, due to the differences        !
! between Linux and Windows file      !
! systems our project will only       !
! run on Linux based machines at      !
! this time.                          !
!                                     !
!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

1. CLONE OUR REPOSITORY:
	
	$git clone https://github.com/reggiegg/CPSC410.git

2. RUN EXECUTABLE .JAR FROM CLONED DIRECTORY USING EITHER OF THE SUPPLIED REPOS AS ARGUMENTS:

	$java -jar sssvnviz.jar monitorsvn

				or

	$java -jar sssvnviz.jar sdSVN

3. VIEW RESULTS

	Double-click on site/index.html
	To view multiple projects in the same browser screen, you will need to put all .csv 
	output files into a single planets.csv
