import http from 'k6/http';
import { check, sleep } from 'k6';
import { uuidv4 } from 'https://jslib.k6.io/k6-utils/1.4.0/index.js';

// Executar com: docker run --rm -i --network=desafio-softdesign-api_DESAFIO_SOFTDESIGN_NETWORK grafana/k6 run - < k6-load-test.js

export const options = {
    stages: [
        { duration: '15s', target: 5 }, // Ramp-up to 5 users
        { duration: '30s', target: 10 },  // Maintain 10 concurrent users
        { duration: '15s', target: 0 },  // Ramp-down to 0 users
    ],
    thresholds: {
        http_req_duration: ['p(95)<500'], // 95% of requests must complete below 500ms
        http_req_failed: ['rate<0.01'],   // Error rate should be less than 1%
    },
};

const BASE_URL = 'http://app_api:8080/v1';

export function setup() {
    console.log('--- Configurando cenários de teste ---');

    // 1. Criar pauta
    const pollPayload = JSON.stringify({
        name: `Pauta Performance ${uuidv4()}`,
        description: "Teste de carga K6"
    });
    const pollRes = http.post(`${BASE_URL}/polls`, pollPayload, { headers: { 'Content-Type': 'application/json' } });
    const pollId = pollRes.json('id');
    console.log(`Pauta criada: ${pollId}`);

    // 2. Abrir Sessão (10 minutos para não expirar durante o teste)
    const expiration = new Date();
    expiration.setMinutes(expiration.getMinutes() + 10);

    const sessionPayload = JSON.stringify({
        pollId: pollId,
        expiration: expiration.toISOString().split('.')[0]
    });
    const sessionRes = http.post(`${BASE_URL}/sessions`, sessionPayload, { headers: { 'Content-Type': 'application/json' } });
    const sessionId = sessionRes.json('id');
    console.log(`Sessão criada: ${sessionId}`);

    return { sessionId };
}

export default function (data) {
    const cpf = generateCpf();

    const associatePayload = JSON.stringify({
        name: `Associado K6 ${__VU}-${__ITER}`,
        taxId: cpf
    });
    
    // Cadastrar!
    const associateRes = http.post(`${BASE_URL}/associates`, associatePayload, { headers: { 'Content-Type': 'application/json' } });
    
    check(associateRes, {
        'associado criado (201)': (r) => r.status === 201 || r.status === 200,
    });

    if (associateRes.status !== 201 && associateRes.status !== 200) {
        console.log(`Falha ao criar associado com CPF ${cpf} - Status: ${associateRes.status}`);
        return; 
    }

    const associateId = associateRes.json('id');

    // 4. Registrar Voto
    const votePayload = JSON.stringify({
        associate: associateId,
        session: data.sessionId,
        vote: Math.random() > 0.5 ? "YES" : "NO"
    });

    const voteRes = http.post(`${BASE_URL}/votes`, votePayload, { headers: { 'Content-Type': 'application/json' } });

    check(voteRes, {
        'voto registrado (201/200)': (r) => r.status === 201 || r.status === 200,
    });

    sleep(1);
}

function generateCpf() {
    const randomParts = Math.floor(Math.random() * 99999999).toString().padStart(8, '0');
    return '999' + randomParts;
}
