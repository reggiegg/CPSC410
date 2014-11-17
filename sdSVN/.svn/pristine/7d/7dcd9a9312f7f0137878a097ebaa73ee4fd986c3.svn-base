#!/bin/bash

user=$USERNAME
web_home="/home/project-web/sd-svn/htdocs"

function die {
  echo $1
  exit 1
}

#To deploy first start a shell using the following:
#ssh -t $user,sd-svn@shell.sf.net create

ssh $user@shell.sourceforge.net ls || die "shell not started"

svn up || die "svn update failed"

mvn clean || die "clean failed"

mvn install || die "install failed"

pushd distribution

mvn2 clean install || die "jnlp failed"

popd


VERSION=`find distribution/full/target -name "*standalone.jar" | cut -d'-' -f3`

echo "deploying version $VERSION..."

# deploy the jar file to frs
scp distribution/full/target/*standalone.jar $user@shell.sourceforge.net:/home/frs/project/s/sd/sd-svn/sdSVN/$VERSION
scp distribution/minimal/target/*standalone.jar $user@shell.sourceforge.net:/home/frs/project/s/sd/sd-svn/sdSVN/$VERSION

# deploy the jnlp files to frs
scp distribution/full/target/jnlp/sdsvn.jnlp $user@shell.sourceforge.net:/home/frs/project/s/sd/sd-svn/sdSVN/$VERSION
scp distribution/minimal/target/jnlp/sdsvn-minimal.jnlp $user@shell.sourceforge.net:/home/frs/project/s/sd/sd-svn/sdSVN/$VERSION

# deploy the rest of the webstart files to htdocs
scp distribution/full/target/jnlp/sdsvn.jnlp $user@shell.sourceforge.net:$web_home/webstart
scp distribution/full/target/jnlp/lib/* $user@shell.sourceforge.net:$web_home/webstart/lib
scp distribution/full/target/jnlp/lib/images/* $user@shell.sourceforge.net:$web_home/webstart/lib/images
scp distribution/minimal/target/jnlp/sdsvn-minimal.jnlp $user@shell.sourceforge.net:$web_home/webstart
scp distribution/minimal/target/jnlp/lib/sdsvn-minimal* $user@shell.sourceforge.net:$web_home/webstart/lib

mvn2 site-deploy || die "site deploy failed"
