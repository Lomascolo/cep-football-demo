#!/bin/sh 

CDIR=$(pwd)

#User have to set his CEP_HOME
CEP_HOME=/home/dilini/CEP/debs-new/wso2cep-3.1.0

cp $CDIR/stream-manager-config.xml $CEP_HOME/repository/conf/
cp $CDIR/debs_challenge_new_1.0.0_1.0.0.jar $CEP_HOME/repository/components/dropins/
cp $CDIR/debs-challenge-new-1.0.0.jar $CEP_HOME/repository/components/lib/
cp -r $CDIR/patch0009 $CEP_HOME/repository/components/patches/
cat $CDIR/siddhi.extension >> $CEP_HOME/repository/conf/siddhi/siddhi.extension
cp -r $CDIR/artifacts/* $CEP_HOME/repository/deployment/server/
cp -r $CDIR/consumers/* $CEP_HOME/samples/consumers/
cp -r $CDIR/debs-input $CEP_HOME/samples/producers/
cp -r $CDIR/fix-to-CEP-870/modules/ws $CEP_HOME/modules/
cp $CDIR/fix-to-CEP-870/dropins/org.jaggeryjs.hostobjects.ws_0.9.0.ALPHA4_wso2v1.jar $CEP_HOME/repository/components/dropins/

cd $CEP_HOME/samples/consumers/uc1-service
ant

cd $CEP_HOME/samples/consumers/uc2-service
ant

cd $CEP_HOME/samples/consumers/game-visualization-service
ant

cp $CDIR

echo "Deployment Successful!"

