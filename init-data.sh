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

curl -s -X POST "$BASE_URL/api/users/$USER1_ID/join/$SERVER1_ID" > /dev/null
echo "Alice joined Maths Server"
curl -s -X POST "$BASE_URL/api/users/$USER1_ID/join/$SERVER2_ID" > /dev/null
echo "Alice joined Algo Server"

curl -s -X POST "$BASE_URL/api/users/$USER2_ID/join/$SERVER1_ID" > /dev/null
echo "Bob joined Maths Server"
curl -s -X POST "$BASE_URL/api/users/$USER2_ID/join/$SERVER3_ID" > /dev/null
echo "Bob joined Physics Server"

curl -s -X POST "$BASE_URL/api/users/$USER3_ID/join/$SERVER2_ID" > /dev/null
echo "Charlie joined Algo Server"
curl -s -X POST "$BASE_URL/api/users/$USER3_ID/join/$SERVER3_ID" > /dev/null
echo "Charlie joined Physics Server"

echo ""
echo "=== Creating Channels ==="

# Maths Server channels
CH1=$(curl -s -X POST "$BASE_URL/api/channels/$SERVER1_ID" \
  -H "Content-Type: application/json" \
  -d '{"name": "general", "topic": "General discussion"}')
CH1_ID=$(echo $CH1 | grep -o '"id":[0-9]*' | head -1 | grep -o '[0-9]*')
echo "Channel 'general' created in Maths Server: ID=$CH1_ID"

CH2=$(curl -s -X POST "$BASE_URL/api/channels/$SERVER1_ID" \
  -H "Content-Type: application/json" \
  -d '{"name": "homework-help", "topic": "Ask for help on exercises"}')
CH2_ID=$(echo $CH2 | grep -o '"id":[0-9]*' | head -1 | grep -o '[0-9]*')
echo "Channel 'homework-help' created in Maths Server: ID=$CH2_ID"

# Algo Server channels
CH3=$(curl -s -X POST "$BASE_URL/api/channels/$SERVER2_ID" \
  -H "Content-Type: application/json" \
  -d '{"name": "general", "topic": "General discussion"}')
CH3_ID=$(echo $CH3 | grep -o '"id":[0-9]*' | head -1 | grep -o '[0-9]*')
echo "Channel 'general' created in Algo Server: ID=$CH3_ID"

CH4=$(curl -s -X POST "$BASE_URL/api/channels/$SERVER2_ID" \
  -H "Content-Type: application/json" \
  -d '{"name": "sorting-algorithms", "topic": "Sorting algorithms discussion"}')
CH4_ID=$(echo $CH4 | grep -o '"id":[0-9]*' | head -1 | grep -o '[0-9]*')
echo "Channel 'sorting-algorithms' created in Algo Server: ID=$CH4_ID"

# Physics Server channels
CH5=$(curl -s -X POST "$BASE_URL/api/channels/$SERVER3_ID" \
  -H "Content-Type: application/json" \
  -d '{"name": "general", "topic": "General discussion"}')
CH5_ID=$(echo $CH5 | grep -o '"id":[0-9]*' | head -1 | grep -o '[0-9]*')
echo "Channel 'general' created in Physics Server: ID=$CH5_ID"

echo ""
echo "=== Posting Messages ==="

# Messages in Maths homework-help channel
curl -s -X POST "$BASE_URL/api/messages/$CH2_ID" \
  -H "Content-Type: application/json" \
  -d "{\"content\": \"How do I solve a second degree equation?\", \"authorId\": $USER1_ID}" > /dev/null
echo "Alice posted in Maths homework-help"

curl -s -X POST "$BASE_URL/api/messages/$CH2_ID" \
  -H "Content-Type: application/json" \
  -d "{\"content\": \"Can someone explain how to solve quadratic equations?\", \"authorId\": $USER2_ID}" > /dev/null
echo "Bob posted in Maths homework-help (similar to Alice's)"

curl -s -X POST "$BASE_URL/api/messages/$CH2_ID" \
  -H "Content-Type: application/json" \
  -d "{\"content\": \"Use the discriminant: b^2 - 4ac to solve any quadratic equation.\", \"authorId\": $USER3_ID}" > /dev/null
echo "Charlie posted answer in Maths homework-help"

# Messages in Algo sorting channel
curl -s -X POST "$BASE_URL/api/messages/$CH4_ID" \
  -H "Content-Type: application/json" \
  -d "{\"content\": \"What is the time complexity of quicksort?\", \"authorId\": $USER1_ID}" > /dev/null
echo "Alice posted in Algo sorting-algorithms"

curl -s -X POST "$BASE_URL/api/messages/$CH4_ID" \
  -H "Content-Type: application/json" \
  -d "{\"content\": \"What is the complexity of merge sort vs quicksort?\", \"authorId\": $USER3_ID}" > /dev/null
echo "Charlie posted in Algo sorting-algorithms (similar to Alice's)"

# Messages in Physics general channel
curl -s -X POST "$BASE_URL/api/messages/$CH5_ID" \
  -H "Content-Type: application/json" \
  -d "{\"content\": \"Does anyone have the lecture notes for thermodynamics?\", \"authorId\": $USER2_ID}" > /dev/null
echo "Bob posted in Physics general"

echo ""
echo "=== Done! ==="
echo "Expected BFS results:"
echo "  Alice  (ID=$USER1_ID) -> should suggest: Physics Server"
echo "  Bob    (ID=$USER2_ID) -> should suggest: Algo Server"
echo "  Charlie(ID=$USER3_ID) -> should suggest: Maths Server"
echo ""
echo "Cosine Similarity test (similar messages in Maths homework-help):"
echo "  Channel ID=$CH2_ID contains similar questions about quadratic equations"
echo "  Channel ID=$CH4_ID contains similar questions about sorting complexity"