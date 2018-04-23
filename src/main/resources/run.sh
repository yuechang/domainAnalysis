server="$( dirname "${BASH_SOURCE[0]}" )" && pwd 
cd $server

case "$1" in  
  
  start)  
    if [ -f "server.pid" ] ; then
			echo "redisSession-0.0.1-SNAPSHOT has been started!"
		else
			java -Xms1g -Xmx1g -Xmn256m -server -jar domainAnalysis.jar >./log/data.log 2>&1 &
			echo $! >$server/server.pid
			echo "domainAnalysis.jar start success!"
		fi
		
    ;; 
  
  stop)  
        kill `cat $server/server.pid`
		rm -rf $server/server.pid
		echo "domainAnalysis.jar stop success!"
		;; 
  
  restart)  
		$0 stop
		sleep 5
		$0 start
		echo "domainAnalysis.jar restart success!"
    ;; 
 
  *) 
    echo "Usage: run.sh {start|stop|restart}"
    ;;
 
esac 
 
exit 0
