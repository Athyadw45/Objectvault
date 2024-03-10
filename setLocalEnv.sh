# Check if the .env file exists
if [ -f .env ]; then
    # Read each line in the .env file
    while IFS= read -r line; do
        # Export each line as an environment variable
        export "$line"
    done < .env
    ENV=local
    echo "Environment variables exported."

else
    echo ".env file not found."
    exit 1
fi
