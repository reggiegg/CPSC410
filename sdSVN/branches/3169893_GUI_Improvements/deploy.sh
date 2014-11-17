#!/bin/bash

user=$USERNAME
web_home="/home/project-web/sd-svn/htdocs"

function die {
  echo $1
  exit 1
}

ssh $user@shell.sourceforge.net ls || die "shell not started"

svn up || die "svn update failed"

mvn clean || die "clean failed"

mvn install || die "install failed"

mvn2 webstart:jnlp || die "jnlp failed"

mvn2 site || die "site failed"

VERSION=`find target -name "*standalone.jar" | cut -d'-' -f2`

echo "deploying version $VERSION..."

# deploy the jar file to frs
scp target/*standalone.jar $user@shell.sourceforge.net:/home/frs/project/s/sd/sd-svn/sdSVN/$VERSION

# deploy the jnlp file to frs
scp target/jnlp/sdsvn.jnlp $user@shell.sourceforge.net:/home/frs/project/s/sd/sd-svn/sdSVN/$VERSION

# deploy the rest of the webstart files to htdocs
scp target/jnlp/sdsvn.jnlp $user@shell.sourceforge.net:$web_home/webstart
scp target/jnlp/lib/* $user@shell.sourceforge.net:$web_home/webstart/lib
scp target/jnlp/lib/images/* $user@shell.sourceforge.net:$web_home/webstart/lib/images

mvn site:deploy || die "site deploy failed"
