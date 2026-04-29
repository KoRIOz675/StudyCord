#!/bin/bash

BASE_URL="http://localhost:8080"

echo "=== Creating Servers ==="

SERVER1=$(curl -s -X POST "$BASE_URL/api/servers" \
  -H "Content-Type: application/json" \
  -d '{"name": "Maths Server", "subject": "Mathematics", "school": "ISEP"}')
SERVER1_ID=$(echo $SERVER1 | grep -o '"id":[0-9]*' | head -1 | grep -o '[0-9]*')
echo "Server 1 created: ID=$SERVER1_ID"

SERVER2=$(curl -s -X POST "$BASE_URL/api/servers" \
  -H "Content-Type: application/json" \
  -d '{"name": "Algo Server", "subject": "Algorithms", "school": "ISEP"}')
SERVER2_ID=$(echo $SERVER2 | grep -o '"id":[0-9]*' | head -1 | grep -o '[0-9]*')
echo "Server 2 created: ID=$SERVER2_ID"

SERVER3=$(curl -s -X POST "$BASE_URL/api/servers" \
  -H "Content-Type: application/json" \
  -d '{"name": "Physics Server", "subject": "Physics", "school": "ISEP"}')
SERVER3_ID=$(echo $SERVER3 | grep -o '"id":[0-9]*' | head -1 | grep -o '[0-9]*')
echo "Server 3 created: ID=$SERVER3_ID"

echo ""
echo "=== Creating Users ==="

USER1=$(curl -s -X POST "$BASE_URL/api/users" \
  -H "Content-Type: application/json" \
  -d '{"username": "alice", "email": "alice@isep.fr", "role": "STUDENT", "school": "ISEP"}')
USER1_ID=$(echo $USER1 | grep -o '"id":[0-9]*' | head -1 | grep -o '[0-9]*')
echo "User 1 created: ID=$USER1_ID"

USER2=$(curl -s -X POST "$BASE_URL/api/users" \
  -H "Content-Type: application/json" \
  -d '{"username": "bob", "email": "bob@isep.fr", "role": "STUDENT", "school": "ISEP"}')
USER2_ID=$(echo $USER2 | grep -o '"id":[0-9]*' | head -1 | grep -o '[0-9]*')
echo "User 2 created: ID=$USER2_ID"

USER3=$(curl -s -X POST "$BASE_URL/api/users" \
  -H "Content-Type: application/json" \
  -d '{"username": "charlie", "email": "charlie@isep.fr", "role": "TEACHER", "school": "ISEP"}')
USER3_ID=$(echo $USER3 | grep -o '"id":[0-9]*' | head -1 | grep -o '[0-9]*')
echo "User 3 created: ID=$USER3_ID"

echo ""
echo "=== Joining Servers ==="

# Alice rejoint Maths et Algo
curl -s -X POST "$BASE_URL/api/users/$USER1_ID/join/$SERVER1_ID" > /dev/null
echo "Alice joined Maths Server"
curl -s -X POST "$BASE_URL/api/users/$USER1_ID/join/$SERVER2_ID" > /dev/null
echo "Alice joined Algo Server"

# Bob rejoint Maths et Physics
curl -s -X POST "$BASE_URL/api/users/$USER2_ID/join/$SERVER1_ID" > /dev/null
echo "Bob joined Maths Server"
curl -s -X POST "$BASE_URL/api/users/$USER2_ID/join/$SERVER3_ID" > /dev/null
echo "Bob joined Physics Server"

# Charlie rejoint Algo et Physics
curl -s -X POST "$BASE_URL/api/users/$USER3_ID/join/$SERVER2_ID" > /dev/null
echo "Charlie joined Algo Server"
curl -s -X POST "$BASE_URL/api/users/$USER3_ID/join/$SERVER3_ID" > /dev/null
echo "Charlie joined Physics Server"

echo ""
echo "=== Done! ==="
echo "Expected BFS results:"
echo "  Alice  (ID=$USER1_ID) -> should suggest: Physics Server"
echo "  Bob    (ID=$USER2_ID) -> should suggest: Algo Server"
echo "  Charlie(ID=$USER3_ID) -> should suggest: Maths Server"