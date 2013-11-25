#!/usr/bin/env sh
adp=10.160.47.117
cache=10.161.133.137
stat=10.161.133.58
log=$1
bin=`which $0`
bin=`dirname ${bin}`
bin=`cd "$bin"; pwd`

tag=`date -d "1 min ago" "+%Y%m%d%H%M"`
tag2=`date -d "2 min ago" "+%Y%m%d%H%M"`
tag3=`date -d "3 min ago" "+%Y%m%d%H%M"`
tag4=`date -d "4 min ago" "+%Y%m%d%H%M"`
tag5=`date -d "5 min ago" "+%Y%m%d%H%M"`

day=${tag:0:8}
out=$bin/logs/$day

if [ -z "$log" ]; then
	log=/data/logs/baicdata/sys/$day/rtb_log_crit_$tag.log,/data/logs/baicdata/sys/$day/rtb_log_crit_$tag2.log,/data/logs/baicdata/sys/$day/rtb_log_crit_$tag3.log,/data/logs/baicdata/sys/$day/rtb_log_crit_$tag4.log,/data/logs/baicdata/sys/$day/rtb_log_crit_$tag5.log
fi

mkdir -p $out

echo $log `date` start

nohup $bin/jdk1.7.0_25/bin/java -jar $bin/Statd.jar \
--adp $adp \
--cache $cache \
--stat $stat \
-f $log \
1>$out/proc_log_$tag.log \
2>&1 \
0</dev/null &

#$bin/jdk1.7.0_25/bin/java -jar $bin/Statd.jar \
#--adp $adp \
#--cache $cache \
#--stat $stat \
#-f $log \
#1>$out/output/$day/proc_log_$tag.log \
#2>$out/output/$day/proc_log_$tag.err \
#0</dev/null


PID=$!
sleep 20m
kill $PID 1>/dev/null 2>&1

echo $log `date` complete
