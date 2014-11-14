HOW TO SET UP AND RUN OUR PROJECT
=================================

I. GETTING AND PREPPING STATSVN:

1. Download StatSVN jar file from SourceForge: http://sourceforge.net/projects/statsvn/
2. Check out an SVN repository (smaller is better), e.g.:
   $ svn checkout http://svn.apache.org/repos/asf/spamassassin/trunk spamassassin
3. Create an SVN log file:
   $ cd spamassassin
   $ svn log -v --xml > logfile.log
4. Run StatSVN on your chosen codebase
   $ java -jar /path/to/statsvn.jar /path/to/module/logfile.log /path/to/module
5. ???

II. GETTING JAVANCSS:

1. Download JavaNCSS from www.kclee.de/clemens/java/javancss/javancss-32.53.zip and get the jar file.
2. ???


III. RUNNING OUR PROGRAM:

1. ???