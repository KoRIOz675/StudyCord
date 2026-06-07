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

# ─────────────────────────────────────────────
# SERVERS
# ─────────────────────────────────────────────
echo ""
echo "=== Creating Servers ==="

SERVER1=$(curl -s -X POST "$BASE_URL/api/servers" \
  -H "Content-Type: application/json" \
  -d '{"name": "Maths Server", "subject": "Mathematics", "school": "ISEP"}')
SERVER1_ID=$(extract_id "$SERVER1")
echo "Maths Server: ID=$SERVER1_ID"

SERVER2=$(curl -s -X POST "$BASE_URL/api/servers" \
  -H "Content-Type: application/json" \
  -d '{"name": "Algo Server", "subject": "Algorithms & Programming", "school": "ISEP"}')
SERVER2_ID=$(extract_id "$SERVER2")
echo "Algo Server: ID=$SERVER2_ID"

SERVER3=$(curl -s -X POST "$BASE_URL/api/servers" \
  -H "Content-Type: application/json" \
  -d '{"name": "Physics Server", "subject": "Physics", "school": "ISEP"}')
SERVER3_ID=$(extract_id "$SERVER3")
echo "Physics Server: ID=$SERVER3_ID"

SERVER4=$(curl -s -X POST "$BASE_URL/api/servers" \
  -H "Content-Type: application/json" \
  -d '{"name": "Chemistry Server", "subject": "Chemistry", "school": "ISEP"}')
SERVER4_ID=$(extract_id "$SERVER4")
echo "Chemistry Server: ID=$SERVER4_ID"

SERVER5=$(curl -s -X POST "$BASE_URL/api/servers" \
  -H "Content-Type: application/json" \
  -d '{"name": "English Server", "subject": "English Literature", "school": "ISEP"}')
SERVER5_ID=$(extract_id "$SERVER5")
echo "English Server: ID=$SERVER5_ID"

# ─────────────────────────────────────────────
# USERS
# ─────────────────────────────────────────────
echo ""
echo "=== Creating Users ==="

USER1=$(curl -s -X POST "$BASE_URL/api/users" \
  -H "Content-Type: application/json" \
  -d '{"username": "alice", "email": "alice@isep.fr", "role": "STUDENT", "school": "ISEP"}')
USER1_ID=$(extract_id "$USER1")
echo "alice   (STUDENT): ID=$USER1_ID"

USER2=$(curl -s -X POST "$BASE_URL/api/users" \
  -H "Content-Type: application/json" \
  -d '{"username": "bob", "email": "bob@isep.fr", "role": "STUDENT", "school": "ISEP"}')
USER2_ID=$(extract_id "$USER2")
echo "bob     (STUDENT): ID=$USER2_ID"

USER3=$(curl -s -X POST "$BASE_URL/api/users" \
  -H "Content-Type: application/json" \
  -d '{"username": "charlie", "email": "charlie@isep.fr", "role": "TEACHER", "school": "ISEP"}')
USER3_ID=$(extract_id "$USER3")
echo "charlie (TEACHER): ID=$USER3_ID"

USER4=$(curl -s -X POST "$BASE_URL/api/users" \
  -H "Content-Type: application/json" \
  -d '{"username": "diana", "email": "diana@isep.fr", "role": "STUDENT", "school": "ISEP"}')
USER4_ID=$(extract_id "$USER4")
echo "diana   (STUDENT): ID=$USER4_ID"

USER5=$(curl -s -X POST "$BASE_URL/api/users" \
  -H "Content-Type: application/json" \
  -d '{"username": "evan", "email": "evan@isep.fr", "role": "TEACHER", "school": "ISEP"}')
USER5_ID=$(extract_id "$USER5")
echo "evan    (TEACHER): ID=$USER5_ID"

USER6=$(curl -s -X POST "$BASE_URL/api/users" \
  -H "Content-Type: application/json" \
  -d '{"username": "fiona", "email": "fiona@isep.fr", "role": "STUDENT", "school": "ISEP"}')
USER6_ID=$(extract_id "$USER6")
echo "fiona   (STUDENT): ID=$USER6_ID"

# ─────────────────────────────────────────────
# SERVER MEMBERSHIPS
# Designed so BFS has meaningful cross-paths:
#   alice   -> Maths, Algo, Physics
#   bob     -> Maths, Physics, English
#   charlie -> Maths, Algo, Physics
#   diana   -> Algo, Chemistry, English
#   evan    -> Physics, Chemistry
#   fiona   -> Maths, Chemistry, English
# ─────────────────────────────────────────────
echo ""
echo "=== Joining Servers ==="

curl -s -X POST "$BASE_URL/api/users/$USER1_ID/join/$SERVER1_ID" > /dev/null && echo "alice   -> Maths"
curl -s -X POST "$BASE_URL/api/users/$USER1_ID/join/$SERVER2_ID" > /dev/null && echo "alice   -> Algo"
curl -s -X POST "$BASE_URL/api/users/$USER1_ID/join/$SERVER3_ID" > /dev/null && echo "alice   -> Physics"

curl -s -X POST "$BASE_URL/api/users/$USER2_ID/join/$SERVER1_ID" > /dev/null && echo "bob     -> Maths"
curl -s -X POST "$BASE_URL/api/users/$USER2_ID/join/$SERVER3_ID" > /dev/null && echo "bob     -> Physics"
curl -s -X POST "$BASE_URL/api/users/$USER2_ID/join/$SERVER5_ID" > /dev/null && echo "bob     -> English"

curl -s -X POST "$BASE_URL/api/users/$USER3_ID/join/$SERVER1_ID" > /dev/null && echo "charlie -> Maths"
curl -s -X POST "$BASE_URL/api/users/$USER3_ID/join/$SERVER2_ID" > /dev/null && echo "charlie -> Algo"
curl -s -X POST "$BASE_URL/api/users/$USER3_ID/join/$SERVER3_ID" > /dev/null && echo "charlie -> Physics"

curl -s -X POST "$BASE_URL/api/users/$USER4_ID/join/$SERVER2_ID" > /dev/null && echo "diana   -> Algo"
curl -s -X POST "$BASE_URL/api/users/$USER4_ID/join/$SERVER4_ID" > /dev/null && echo "diana   -> Chemistry"
curl -s -X POST "$BASE_URL/api/users/$USER4_ID/join/$SERVER5_ID" > /dev/null && echo "diana   -> English"

curl -s -X POST "$BASE_URL/api/users/$USER5_ID/join/$SERVER3_ID" > /dev/null && echo "evan    -> Physics"
curl -s -X POST "$BASE_URL/api/users/$USER5_ID/join/$SERVER4_ID" > /dev/null && echo "evan    -> Chemistry"

curl -s -X POST "$BASE_URL/api/users/$USER6_ID/join/$SERVER1_ID" > /dev/null && echo "fiona   -> Maths"
curl -s -X POST "$BASE_URL/api/users/$USER6_ID/join/$SERVER4_ID" > /dev/null && echo "fiona   -> Chemistry"
curl -s -X POST "$BASE_URL/api/users/$USER6_ID/join/$SERVER5_ID" > /dev/null && echo "fiona   -> English"

# ─────────────────────────────────────────────
# CHANNELS
# ─────────────────────────────────────────────
echo ""
echo "=== Creating Channels ==="

# Maths
MATH_GENERAL=$(curl -s -X POST "$BASE_URL/api/channels/$SERVER1_ID" \
  -H "Content-Type: application/json" \
  -d '{"name": "general", "topic": "General discussion"}')
MATH_GENERAL_ID=$(extract_id "$MATH_GENERAL")
echo "Maths #general: ID=$MATH_GENERAL_ID"

MATH_HOMEWORK=$(curl -s -X POST "$BASE_URL/api/channels/$SERVER1_ID" \
  -H "Content-Type: application/json" \
  -d '{"name": "homework-help", "topic": "Ask for help on exercises"}')
MATH_HOMEWORK_ID=$(extract_id "$MATH_HOMEWORK")
echo "Maths #homework-help: ID=$MATH_HOMEWORK_ID"

MATH_EXAMS=$(curl -s -X POST "$BASE_URL/api/channels/$SERVER1_ID" \
  -H "Content-Type: application/json" \
  -d '{"name": "exam-prep", "topic": "Exam preparation and past papers"}')
MATH_EXAMS_ID=$(extract_id "$MATH_EXAMS")
echo "Maths #exam-prep: ID=$MATH_EXAMS_ID"

# Algo
ALGO_GENERAL=$(curl -s -X POST "$BASE_URL/api/channels/$SERVER2_ID" \
  -H "Content-Type: application/json" \
  -d '{"name": "general", "topic": "General discussion"}')
ALGO_GENERAL_ID=$(extract_id "$ALGO_GENERAL")
echo "Algo #general: ID=$ALGO_GENERAL_ID"

ALGO_SORTING=$(curl -s -X POST "$BASE_URL/api/channels/$SERVER2_ID" \
  -H "Content-Type: application/json" \
  -d '{"name": "sorting-algorithms", "topic": "Sorting algorithms discussion"}')
ALGO_SORTING_ID=$(extract_id "$ALGO_SORTING")
echo "Algo #sorting-algorithms: ID=$ALGO_SORTING_ID"

ALGO_DS=$(curl -s -X POST "$BASE_URL/api/channels/$SERVER2_ID" \
  -H "Content-Type: application/json" \
  -d '{"name": "data-structures", "topic": "Data structures and complexity"}')
ALGO_DS_ID=$(extract_id "$ALGO_DS")
echo "Algo #data-structures: ID=$ALGO_DS_ID"

# Physics
PHYS_GENERAL=$(curl -s -X POST "$BASE_URL/api/channels/$SERVER3_ID" \
  -H "Content-Type: application/json" \
  -d '{"name": "general", "topic": "General discussion"}')
PHYS_GENERAL_ID=$(extract_id "$PHYS_GENERAL")
echo "Physics #general: ID=$PHYS_GENERAL_ID"

PHYS_THERMO=$(curl -s -X POST "$BASE_URL/api/channels/$SERVER3_ID" \
  -H "Content-Type: application/json" \
  -d '{"name": "thermodynamics", "topic": "Thermodynamics and heat transfer"}')
PHYS_THERMO_ID=$(extract_id "$PHYS_THERMO")
echo "Physics #thermodynamics: ID=$PHYS_THERMO_ID"

PHYS_MECH=$(curl -s -X POST "$BASE_URL/api/channels/$SERVER3_ID" \
  -H "Content-Type: application/json" \
  -d '{"name": "mechanics", "topic": "Classical mechanics and dynamics"}')
PHYS_MECH_ID=$(extract_id "$PHYS_MECH")
echo "Physics #mechanics: ID=$PHYS_MECH_ID"

# Chemistry
CHEM_GENERAL=$(curl -s -X POST "$BASE_URL/api/channels/$SERVER4_ID" \
  -H "Content-Type: application/json" \
  -d '{"name": "general", "topic": "General discussion"}')
CHEM_GENERAL_ID=$(extract_id "$CHEM_GENERAL")
echo "Chemistry #general: ID=$CHEM_GENERAL_ID"

CHEM_ORGANIC=$(curl -s -X POST "$BASE_URL/api/channels/$SERVER4_ID" \
  -H "Content-Type: application/json" \
  -d '{"name": "organic-chemistry", "topic": "Organic chemistry reactions and mechanisms"}')
CHEM_ORGANIC_ID=$(extract_id "$CHEM_ORGANIC")
echo "Chemistry #organic-chemistry: ID=$CHEM_ORGANIC_ID"

# English
ENG_GENERAL=$(curl -s -X POST "$BASE_URL/api/channels/$SERVER5_ID" \
  -H "Content-Type: application/json" \
  -d '{"name": "general", "topic": "General discussion"}')
ENG_GENERAL_ID=$(extract_id "$ENG_GENERAL")
echo "English #general: ID=$ENG_GENERAL_ID"

ENG_ESSAYS=$(curl -s -X POST "$BASE_URL/api/channels/$SERVER5_ID" \
  -H "Content-Type: application/json" \
  -d '{"name": "essay-writing", "topic": "Essay structure and writing tips"}')
ENG_ESSAYS_ID=$(extract_id "$ENG_ESSAYS")
echo "English #essay-writing: ID=$ENG_ESSAYS_ID"

# ─────────────────────────────────────────────
# MESSAGES
# Each non-general channel has intentional similar pairs
# to make cosine similarity testing meaningful.
# ─────────────────────────────────────────────
echo ""
echo "=== Posting Messages ==="

# --- Maths #homework-help (similar: quadratic equations) ---
curl -s -X POST "$BASE_URL/api/messages/$MATH_HOMEWORK_ID" -H "Content-Type: application/json" \
  -d "{\"content\": \"How do I solve a second degree equation?\", \"authorId\": $USER1_ID}" > /dev/null
echo "alice   -> Maths #homework-help"

curl -s -X POST "$BASE_URL/api/messages/$MATH_HOMEWORK_ID" -H "Content-Type: application/json" \
  -d "{\"content\": \"Can someone explain how to solve quadratic equations?\", \"authorId\": $USER2_ID}" > /dev/null
echo "bob     -> Maths #homework-help"

curl -s -X POST "$BASE_URL/api/messages/$MATH_HOMEWORK_ID" -H "Content-Type: application/json" \
  -d "{\"content\": \"I keep getting wrong answers solving quadratic equations, any help?\", \"authorId\": $USER6_ID}" > /dev/null
echo "fiona   -> Maths #homework-help"

curl -s -X POST "$BASE_URL/api/messages/$MATH_HOMEWORK_ID" -H "Content-Type: application/json" \
  -d "{\"content\": \"Use the discriminant b squared minus 4ac to solve any quadratic equation.\", \"authorId\": $USER3_ID}" > /dev/null
echo "charlie -> Maths #homework-help (answer)"

# --- Maths #exam-prep (similar: calculus exam) ---
curl -s -X POST "$BASE_URL/api/messages/$MATH_EXAMS_ID" -H "Content-Type: application/json" \
  -d "{\"content\": \"When is the next calculus exam scheduled?\", \"authorId\": $USER1_ID}" > /dev/null
echo "alice   -> Maths #exam-prep"

curl -s -X POST "$BASE_URL/api/messages/$MATH_EXAMS_ID" -H "Content-Type: application/json" \
  -d "{\"content\": \"Does the calculus exam cover integration by parts?\", \"authorId\": $USER6_ID}" > /dev/null
echo "fiona   -> Maths #exam-prep"

curl -s -X POST "$BASE_URL/api/messages/$MATH_EXAMS_ID" -H "Content-Type: application/json" \
  -d "{\"content\": \"The exam covers integration, derivatives, and limits.\", \"authorId\": $USER3_ID}" > /dev/null
echo "charlie -> Maths #exam-prep (answer)"

# --- Algo #sorting-algorithms (similar: quicksort complexity) ---
curl -s -X POST "$BASE_URL/api/messages/$ALGO_SORTING_ID" -H "Content-Type: application/json" \
  -d "{\"content\": \"What is the time complexity of quicksort?\", \"authorId\": $USER1_ID}" > /dev/null
echo "alice   -> Algo #sorting-algorithms"

curl -s -X POST "$BASE_URL/api/messages/$ALGO_SORTING_ID" -H "Content-Type: application/json" \
  -d "{\"content\": \"What is the complexity of merge sort versus quicksort?\", \"authorId\": $USER4_ID}" > /dev/null
echo "diana   -> Algo #sorting-algorithms"

curl -s -X POST "$BASE_URL/api/messages/$ALGO_SORTING_ID" -H "Content-Type: application/json" \
  -d "{\"content\": \"Why is merge sort preferred over quicksort for linked lists?\", \"authorId\": $USER4_ID}" > /dev/null
echo "diana   -> Algo #sorting-algorithms"

curl -s -X POST "$BASE_URL/api/messages/$ALGO_SORTING_ID" -H "Content-Type: application/json" \
  -d "{\"content\": \"Quicksort has average complexity O n log n but worst case O n squared.\", \"authorId\": $USER3_ID}" > /dev/null
echo "charlie -> Algo #sorting-algorithms (answer)"

# --- Algo #data-structures (similar: binary search tree) ---
curl -s -X POST "$BASE_URL/api/messages/$ALGO_DS_ID" -H "Content-Type: application/json" \
  -d "{\"content\": \"What is the difference between a binary tree and a binary search tree?\", \"authorId\": $USER1_ID}" > /dev/null
echo "alice   -> Algo #data-structures"

curl -s -X POST "$BASE_URL/api/messages/$ALGO_DS_ID" -H "Content-Type: application/json" \
  -d "{\"content\": \"Can someone explain binary search tree insertion?\", \"authorId\": $USER4_ID}" > /dev/null
echo "diana   -> Algo #data-structures"

curl -s -X POST "$BASE_URL/api/messages/$ALGO_DS_ID" -H "Content-Type: application/json" \
  -d "{\"content\": \"What is the time complexity for searching in a balanced binary search tree?\", \"authorId\": $USER1_ID}" > /dev/null
echo "alice   -> Algo #data-structures"

curl -s -X POST "$BASE_URL/api/messages/$ALGO_DS_ID" -H "Content-Type: application/json" \
  -d "{\"content\": \"In a binary search tree, left child is always smaller than the parent node.\", \"authorId\": $USER3_ID}" > /dev/null
echo "charlie -> Algo #data-structures (answer)"

# --- Physics #thermodynamics (similar: entropy) ---
curl -s -X POST "$BASE_URL/api/messages/$PHYS_THERMO_ID" -H "Content-Type: application/json" \
  -d "{\"content\": \"Can someone explain what entropy means in thermodynamics?\", \"authorId\": $USER2_ID}" > /dev/null
echo "bob     -> Physics #thermodynamics"

curl -s -X POST "$BASE_URL/api/messages/$PHYS_THERMO_ID" -H "Content-Type: application/json" \
  -d "{\"content\": \"What is the second law of thermodynamics and how does entropy relate to it?\", \"authorId\": $USER1_ID}" > /dev/null
echo "alice   -> Physics #thermodynamics"

curl -s -X POST "$BASE_URL/api/messages/$PHYS_THERMO_ID" -H "Content-Type: application/json" \
  -d "{\"content\": \"Does anyone have the lecture notes for thermodynamics?\", \"authorId\": $USER2_ID}" > /dev/null
echo "bob     -> Physics #thermodynamics"

curl -s -X POST "$BASE_URL/api/messages/$PHYS_THERMO_ID" -H "Content-Type: application/json" \
  -d "{\"content\": \"Entropy measures disorder in a thermodynamic system and always increases.\", \"authorId\": $USER5_ID}" > /dev/null
echo "evan    -> Physics #thermodynamics (answer)"

# --- Physics #mechanics (similar: Newton force) ---
curl -s -X POST "$BASE_URL/api/messages/$PHYS_MECH_ID" -H "Content-Type: application/json" \
  -d "{\"content\": \"How do I apply Newton second law to solve force problems?\", \"authorId\": $USER2_ID}" > /dev/null
echo "bob     -> Physics #mechanics"

curl -s -X POST "$BASE_URL/api/messages/$PHYS_MECH_ID" -H "Content-Type: application/json" \
  -d "{\"content\": \"How do I calculate the net force when multiple forces act on an object?\", \"authorId\": $USER1_ID}" > /dev/null
echo "alice   -> Physics #mechanics"

curl -s -X POST "$BASE_URL/api/messages/$PHYS_MECH_ID" -H "Content-Type: application/json" \
  -d "{\"content\": \"Newton second law states that force equals mass times acceleration.\", \"authorId\": $USER5_ID}" > /dev/null
echo "evan    -> Physics #mechanics (answer)"

# --- Chemistry #organic-chemistry (similar: SN1 SN2) ---
curl -s -X POST "$BASE_URL/api/messages/$CHEM_ORGANIC_ID" -H "Content-Type: application/json" \
  -d "{\"content\": \"What is the difference between SN1 and SN2 reactions?\", \"authorId\": $USER4_ID}" > /dev/null
echo "diana   -> Chemistry #organic-chemistry"

curl -s -X POST "$BASE_URL/api/messages/$CHEM_ORGANIC_ID" -H "Content-Type: application/json" \
  -d "{\"content\": \"Can someone explain the SN1 substitution reaction mechanism?\", \"authorId\": $USER6_ID}" > /dev/null
echo "fiona   -> Chemistry #organic-chemistry"

curl -s -X POST "$BASE_URL/api/messages/$CHEM_ORGANIC_ID" -H "Content-Type: application/json" \
  -d "{\"content\": \"Which conditions favor SN1 over SN2?\", \"authorId\": $USER4_ID}" > /dev/null
echo "diana   -> Chemistry #organic-chemistry"

curl -s -X POST "$BASE_URL/api/messages/$CHEM_ORGANIC_ID" -H "Content-Type: application/json" \
  -d "{\"content\": \"SN1 is a two-step reaction while SN2 is a concerted one-step mechanism.\", \"authorId\": $USER5_ID}" > /dev/null
echo "evan    -> Chemistry #organic-chemistry (answer)"

# --- English #essay-writing (similar: argumentative essay) ---
curl -s -X POST "$BASE_URL/api/messages/$ENG_ESSAYS_ID" -H "Content-Type: application/json" \
  -d "{\"content\": \"How do I structure a good argumentative essay?\", \"authorId\": $USER2_ID}" > /dev/null
echo "bob     -> English #essay-writing"

curl -s -X POST "$BASE_URL/api/messages/$ENG_ESSAYS_ID" -H "Content-Type: application/json" \
  -d "{\"content\": \"What is the best way to write an introduction for an essay?\", \"authorId\": $USER6_ID}" > /dev/null
echo "fiona   -> English #essay-writing"

curl -s -X POST "$BASE_URL/api/messages/$ENG_ESSAYS_ID" -H "Content-Type: application/json" \
  -d "{\"content\": \"How long should the introduction of an argumentative essay be?\", \"authorId\": $USER2_ID}" > /dev/null
echo "bob     -> English #essay-writing"

curl -s -X POST "$BASE_URL/api/messages/$ENG_ESSAYS_ID" -H "Content-Type: application/json" \
  -d "{\"content\": \"A good argumentative essay needs a clear thesis, supporting arguments, and a conclusion.\", \"authorId\": $USER4_ID}" > /dev/null
echo "diana   -> English #essay-writing (answer)"

# ─────────────────────────────────────────────
# REINDEX
# Indexing is no longer automatic on message post.
# Run a batch reindex for every channel that has messages.
# ─────────────────────────────────────────────
echo ""
echo "=== Reindexing channels ==="

for CH_ID in $MATH_HOMEWORK_ID $MATH_EXAMS_ID $ALGO_SORTING_ID $ALGO_DS_ID $PHYS_THERMO_ID $PHYS_MECH_ID $CHEM_ORGANIC_ID $ENG_ESSAYS_ID; do
  RESULT=$(curl -s -X POST "$BASE_URL/api/algorithms/reindex-channel/$CH_ID")
  echo "  Channel $CH_ID: $RESULT"
done

# ─────────────────────────────────────────────
# SUMMARY
# ─────────────────────────────────────────────
echo ""
echo "=== Init complete! ==="
echo ""
echo "Servers:"
echo "  Maths     ID=$SERVER1_ID"
echo "  Algo      ID=$SERVER2_ID"
echo "  Physics   ID=$SERVER3_ID"
echo "  Chemistry ID=$SERVER4_ID"
echo "  English   ID=$SERVER5_ID"
echo ""
echo "Users:"
echo "  alice   (STUDENT)  ID=$USER1_ID  -> Maths, Algo, Physics"
echo "  bob     (STUDENT)  ID=$USER2_ID  -> Maths, Physics, English"
echo "  charlie (TEACHER)  ID=$USER3_ID  -> Maths, Algo, Physics"
echo "  diana   (STUDENT)  ID=$USER4_ID  -> Algo, Chemistry, English"
echo "  evan    (TEACHER)  ID=$USER5_ID  -> Physics, Chemistry"
echo "  fiona   (STUDENT)  ID=$USER6_ID  -> Maths, Chemistry, English"
echo ""
echo "BFS expected suggestions:"
echo "  alice   (Maths, Algo, Physics)      -> Chemistry (via diana/Algo), English (via bob/Maths)"
echo "  bob     (Maths, Physics, English)   -> Algo (via alice/Maths), Chemistry (via evan/Physics)"
echo "  charlie (Maths, Algo, Physics)      -> Chemistry (via diana/Algo), English (via bob/Maths)"
echo "  diana   (Algo, Chemistry, English)  -> Maths (via alice/Algo), Physics (via evan/Chemistry)"
echo "  evan    (Physics, Chemistry)        -> Maths (via alice/Physics), English (via diana/Chemistry)"
echo "  fiona   (Maths, Chemistry, English) -> Algo (via alice/Maths), Physics (via bob/Maths)"
echo ""
echo "Cosine similarity test queries:"
echo "  Maths  #homework-help   ID=$MATH_HOMEWORK_ID  -> query: 'quadratic equation'"
echo "  Maths  #exam-prep       ID=$MATH_EXAMS_ID     -> query: 'calculus exam'"
echo "  Algo   #sorting         ID=$ALGO_SORTING_ID   -> query: 'quicksort complexity'"
echo "  Algo   #data-structures ID=$ALGO_DS_ID        -> query: 'binary search tree'"
echo "  Phys   #thermodynamics  ID=$PHYS_THERMO_ID    -> query: 'entropy thermodynamics'"
echo "  Phys   #mechanics       ID=$PHYS_MECH_ID      -> query: 'Newton force'"
echo "  Chem   #organic         ID=$CHEM_ORGANIC_ID   -> query: 'SN1 reaction'"
echo "  Eng    #essays          ID=$ENG_ESSAYS_ID     -> query: 'argumentative essay'"
