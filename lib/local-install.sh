function install {
	echo "Installing $3 ($2:$4)"
	echo ""

	mvn install:install-file \
        	-Dfile="$1" \
	        -DgroupId="$2" \
        	-DartifactId="$3" \
	        -Dversion="$4" \
        	-Dpackaging=jar -DgeneratePom=true

	echo ""
	echo ""
	echo ""
}


install "./paper.jar" "com.destroystokyo" "paper" "1.7-R1"
install "./waterfall.jar" "com.destroystokyo" "waterfall" "1.11-R1"

