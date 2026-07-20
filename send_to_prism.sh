# Script to send the build jar to the Tainted Forces instance
# Assumes you manually set $PRISM_INSTANCE_DIRECTORY in ~/.bashsrc or equivalent
# Version should be the one in gradle.properties
read -p "Version Number : " version
rm "$PRISM_INSTANCE_DIRECTORY/Tainted Forces/minecraft/mods/"*tfutils*
mv -f ./build/libs/!tfutils-${version}.jar "$PRISM_INSTANCE_DIRECTORY/Tainted Forces/minecraft/mods/"
echo "Sent to Prism"