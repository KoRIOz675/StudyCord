#!/bin/bash

BASE_URL="http://localhost:8080"

extract_id() {
  echo "$1" | grep -o '"id":[0-9]*' | head -1 | cut -d':' -f2
}

echo "=== Waiting for Spring Boot to be ready ==="
until curl -s "$BASE_URL/api/servers" > /dev/null; do
  echo "Waiting..."
  sleep 2
done
echo "Backend is ready!"

SERVER1=$(curl -s -X POST "$BASE_URL/api/servers" \
  -H "Content-Type: application/json" \
  -d '{"name": "Maths Server", "subject": "Mathematics", "school": "ISEP"}')
SERVER1_ID=$(extract_id "$SERVER1")
echo "Server 1 created: ID=$SERVER1_ID"

SERVER2=$(curl -s -X POST "$BASE_URL/api/servers" \
  -H "Content-Type: application/json" \
  -d '{"name": "Algo Server", "subject": "Algorithms", "school": "ISEP"}')
SERVER2_ID=$(extract_id "$SERVER2")
echo "Server 2 created: ID=$SERVER2_ID"

SERVER3=$(curl -s -X POST "$BASE_URL/api/servers" \
  -H "Content-Type: application/json" \
  -d '{"name": "Physics Server", "subject": "Physics", "school": "ISEP"}')
SERVER3_ID=$(extract_id "$SERVER3")
echo "Server 3 created: ID=$SERVER3_ID"

echo ""
echo "=== Creating Users ==="

USER1=$(curl -s -X POST "$BASE_URL/api/users" \
  -H "Content-Type: application/json" \
  -d '{"username": "alice", "email": "alice@isep.fr", "role": "STUDENT", "school": "ISEP"}')
USER1_ID=$(extract_id "$USER1")
echo "User 1 created: ID=$USER1_ID"

USER2=$(curl -s -X POST "$BASE_URL/api/users" \
  -H "Content-Type: application/json" \
  -d '{"username": "bob", "email": "bob@isep.fr", "role": "STUDENT", "school": "ISEP"}')
USER2_ID=$(extract_id "$USER2")
echo "User 2 created: ID=$USER2_ID"

USER3=$(curl -s -X POST "$BASE_URL/api/users" \
  -H "Content-Type: application/json" \
  -d '{"username": "charlie", "email": "charlie@isep.fr", "role": "TEACHER", "school": "ISEP"}')
USER3_ID=$(extract_id "$USER3")
echo "User 3 created: ID=$USER3_ID"

echo ""
echo "=== Joining Servers ==="
echo "Debug: USER1_ID=$USER1_ID, USER2_ID=$USER2_ID, USER3_ID=$USER3_ID"
echo "Debug: SERVER1_ID=$SERVER1_ID, SERVER2_ID=$SERVER2_ID, SERVER3_ID=$SERVER3_ID"

echo ""
echo "Alice joining Maths Server (user=$USER1_ID, server=$SERVER1_ID):"
curl -s -X POST "$BASE_URL/api/users/$USER1_ID/join/$SERVER1_ID"
echo ""

echo "Alice joining Algo Server (user=$USER1_ID, server=$SERVER2_ID):"
curl -s -X POST "$BASE_URL/api/users/$USER1_ID/join/$SERVER2_ID"
echo ""

echo "Bob joining Maths Server (user=$USER2_ID, server=$SERVER1_ID):"
curl -s -X POST "$BASE_URL/api/users/$USER2_ID/join/$SERVER1_ID"
echo ""

echo "Bob joining Physics Server (user=$USER2_ID, server=$SERVER3_ID):"
curl -s -X POST "$BASE_URL/api/users/$USER2_ID/join/$SERVER3_ID"
echo ""

echo "Charlie joining Algo Server (user=$USER3_ID, server=$SERVER2_ID):"
curl -s -X POST "$BASE_URL/api/users/$USER3_ID/join/$SERVER2_ID"
echo ""

echo "Charlie joining Physics Server (user=$USER3_ID, server=$SERVER3_ID):"
curl -s -X POST "$BASE_URL/api/users/$USER3_ID/join/$SERVER3_ID"
echo ""

echo ""
echo "=== Creating Channels ==="

# Maths Server channels
CH1=$(curl -s -X POST "$BASE_URL/api/channels/$SERVER1_ID" \
  -H "Content-Type: application/json" \
  -d '{"name": "general", "topic": "General discussion"}')
CH1_ID=$(extract_id "$CH1")
echo "Channel 'general' created in Maths Server: ID=$CH1_ID"

CH2=$(curl -s -X POST "$BASE_URL/api/channels/$SERVER1_ID" \
  -H "Content-Type: application/json" \
  -d '{"name": "homework-help", "topic": "Ask for help on exercises"}')
CH2_ID=$(extract_id "$CH2")
echo "Channel 'homework-help' created in Maths Server: ID=$CH2_ID"

# Algo Server channels
CH3=$(curl -s -X POST "$BASE_URL/api/channels/$SERVER2_ID" \
  -H "Content-Type: application/json" \
  -d '{"name": "general", "topic": "General discussion"}')
CH3_ID=$(extract_id "$CH3")
echo "Channel 'general' created in Algo Server: ID=$CH3_ID"

CH4=$(curl -s -X POST "$BASE_URL/api/channels/$SERVER2_ID" \
  -H "Content-Type: application/json" \
  -d '{"name": "sorting-algorithms", "topic": "Sorting algorithms discussion"}')
CH4_ID=$(extract_id "$CH4")
echo "Channel 'sorting-algorithms' created in Algo Server: ID=$CH4_ID"

# Physics Server channels
CH5=$(curl -s -X POST "$BASE_URL/api/channels/$SERVER3_ID" \
  -H "Content-Type: application/json" \
  -d '{"name": "general", "topic": "General discussion"}')
CH5_ID=$(extract_id "$CH5")
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