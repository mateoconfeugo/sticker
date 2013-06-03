export PATH=/opt/local/bin:/opt/local/sbin:$HOME/bin:$PATH

PERL5LIB=$PERL5LIB:$HOME/perl5
export PERL5LIB

# Landing Site Business Server environment variables
export LSBS_USER=$USER
export LSBS_GROUP=$USER
export LSBS_HOME=$HOME
export LSBS_ROOT_DIR=$HOME
export LSBS_CFG_DIR=$LSBS_ROOT_DIR/website/config
export LSBS_WEBSITE=$LSBS_ROOT_DIR/website
# TODO:  retrieve these in most secure manner test values 
export LSBS_DB_ADDRESS="127.0.0.1"
export LSBS_DB_USER="root"
export LSBS_DB_PASSWORD="test123"
export LSBS_DB_NAME="pcs"
export LSBS_MONITORING_ADDRESS="127.0.0.1"
