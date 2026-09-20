/*
 * Copyright (c) 2026 Mark Hunter
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

export interface SubordinatedMiddleware {
  name: string;
  technology: string;
  ports: string;
  role: string;
  nodes?: string[];
  metrics?: { label: string; value: string }[];
  description: string;
}

export interface SubsystemChildNode {
  id: string;
  name: string;
  englishTitle: string;
  description: string;
}

export interface AuthoritativeSubsystem {
  id: string;
  name: string;
  englishTitle: string;
  description: string;
  areaId: string;
  areaName: string;
  children?: SubsystemChildNode[];
  middleware?: SubordinatedMiddleware;
}

export interface ArchitecturalArea {
  id: string;
  name: string;
  englishTitle: string;
  description: string;
  subsystemIds: string[];
}

export const HARMONIA_ARCHITECTURAL_AREAS: ArchitecturalArea[] = [
  {
    id: 'integration-transport',
    name: 'Integration & Transport',
    englishTitle: 'Gateways, Ingress & Message Brokering',
    description: 'Protocol adaptation, wire validation, fan-out delivery, and message transport',
    subsystemIds: ['pylai', 'petasos']
  },
  {
    id: 'execution-processing',
    name: 'Execution & Processing',
    englishTitle: 'Workflow Orchestration & Task Execution',
    description: 'Activity dispatching, transformation pipelines, and stateful workflow sequences',
    subsystemIds: ['energeia']
  },
  {
    id: 'information-state',
    name: 'Information & State',
    englishTitle: 'Canonical Data Models, Caching & Persistence',
    description: 'In-memory operational cache grids, HAPI FHIR R5 schemas, and durable relational storage',
    subsystemIds: ['calliope', 'mneme', 'mnemosyne']
  },
  {
    id: 'security-policy',
    name: 'Security & Policy',
    englishTitle: 'Authorization, RBAC & Policy Enforcement',
    description: 'Default-deny authorization engine, security context propagation, and audit governance',
    subsystemIds: ['themis']
  },
  {
    id: 'collaboration',
    name: 'Collaboration',
    englishTitle: 'Matrix Homeserver & Application Service Bridge',
    description: 'Healthcare communication projection, Matrix room lifecycles, and event exchange',
    subsystemIds: ['agora']
  },
  {
    id: 'presentation',
    name: 'Presentation',
    englishTitle: 'UI Gateways & Single Page Applications',
    description: 'Presentation services, BEFE REST gateways, and Iris operator workbenches',
    subsystemIds: ['iris']
  }
];

export const AUTHORITATIVE_SUBSYSTEMS: Record<string, AuthoritativeSubsystem> = {
  pylai: {
    id: 'pylai',
    name: 'Pylai',
    englishTitle: 'Interface Gateways',
    description: 'HL7 MLLP & FHIR Inbound/Outbound Protocol Gateways',
    areaId: 'integration-transport',
    areaName: 'Integration & Transport',
    children: [
      { id: 'pylai-mllp-in', name: 'MLLP Inbound Gateway', englishTitle: 'Ingress Interface', description: 'Dual-write ACK gateway on ports 2575 / 8084' },
      { id: 'pylai-mllp-out-his', name: 'MLLP Outbound HIS', englishTitle: 'HIS Distribution Interface', description: 'Outbound HL7 v2 gateway on port 8087' },
      { id: 'pylai-mllp-out-lis', name: 'MLLP Outbound LIS', englishTitle: 'LIS Distribution Interface', description: 'Outbound HL7 v2 gateway on port 8088' },
      { id: 'pylai-fhir-registry', name: 'FHIR Provider Registry Gateway', englishTitle: 'REST Registry Ingress', description: 'Practitioner & Organization endpoint on port 8089' }
    ]
  },
  petasos: {
    id: 'petasos',
    name: 'Petasos',
    englishTitle: 'Messaging & Transport',
    description: 'Resilient Messaging Abstraction & ActiveMQ Artemis Broker',
    areaId: 'integration-transport',
    areaName: 'Integration & Transport',
    middleware: {
      name: 'Apache ActiveMQ Artemis Cluster',
      technology: 'ActiveMQ Artemis 2.33',
      ports: '61616 (CORE), 5672 (AMQP)',
      role: 'Enterprise Message Broker & Queue Store',
      nodes: ['petasos-artemis-node1 (Primary)', 'petasos-artemis-node2 (Backup)'],
      metrics: [
        { label: 'Cluster Topology', value: 'High-Availability Primary/Replica Pair' },
        { label: 'Ingress Queues', value: 'petasos.queue.pylai.mllp.in, petasos.queue.ponos.dispatch' },
        { label: 'Outbound Queues', value: 'petasos.queue.mllp.outbound.his, petasos.queue.mllp.outbound.lis' },
        { label: 'DLQ / Expiry', value: 'ActiveMQ.DLQ (Dead Letter), ActiveMQ.Expiry' }
      ],
      description: 'Underlying resilient broker cluster handling durable asynchronous queue delivery across Harmonia'
    }
  },
  energeia: {
    id: 'energeia',
    name: 'Energeia',
    englishTitle: 'Workflow & Activity Execution',
    description: 'Task Processing, Ergon Activity & Praxis Workflow Orchestration',
    areaId: 'execution-processing',
    areaName: 'Execution & Processing',
    children: [
      { id: 'ponos', name: 'Ponos', englishTitle: 'Task Processing Engine', description: 'Ponos Task Processor & Activity Handler workers' },
      { id: 'praxis', name: 'Praxis', englishTitle: 'Workflow Sequences', description: 'Praxis Workflow Engine & Pragma State Coordinator' },
      { id: 'ergon', name: 'Ergon', englishTitle: 'Activity Units', description: 'Task / Work Unit Activities & Payload Transformers' },
      { id: 'pragma', name: 'Pragma', englishTitle: 'Task Instances', description: 'Task Instances & Runtime Checkpoints' }
    ]
  },
  calliope: {
    id: 'calliope',
    name: 'Calliope',
    englishTitle: 'Canonical Models & Schemas',
    description: 'Canonical Schemas, Transformers & Clinical HL7/FHIR Models',
    areaId: 'information-state',
    areaName: 'Information & State'
  },
  mneme: {
    id: 'mneme',
    name: 'Mneme',
    englishTitle: 'Operational In-Memory Cache Grid',
    description: 'Infinispan Distributed Replicated In-Memory Cache Grid',
    areaId: 'information-state',
    areaName: 'Information & State',
    middleware: {
      name: 'Infinispan Clustered Cache Grid',
      technology: 'Infinispan 15.0',
      ports: '11222 (Hot Rod Client), 11223 (JGroups Mesh)',
      role: 'Distributed In-Memory Replicated Data Grid',
      nodes: ['mneme-cache-node-1', 'mneme-cache-node-2'],
      metrics: [
        { label: 'Clustering Mode', value: 'Distributed & Replicated Sync' },
        { label: 'Operations Caches', value: 'modulestatus-cache, tasksequence-cache, messagequeue-cache' },
        { label: 'Workflow Caches', value: 'task-cache, communication-cache' },
        { label: 'FHIR Model Caches', value: 'provenance-cache, person-cache, practitioner-cache, organization-cache' }
      ],
      description: 'Ultra-low-latency distributed data grid providing authoritative fast cache access and cluster lock coordination'
    }
  },
  mnemosyne: {
    id: 'mnemosyne',
    name: 'Mnemosyne',
    englishTitle: 'Durable Relational Persistence',
    description: 'Clinical & Operational HAPI FHIR R5 Persistence',
    areaId: 'information-state',
    areaName: 'Information & State',
    middleware: {
      name: 'PostgreSQL & HAPI FHIR JPA Storage',
      technology: 'PostgreSQL 16 / HAPI FHIR JPA 7.4',
      ports: '5432 (PostgreSQL), 8081/8082 (Clinical FHIR), 8085/8086 (Operations JPA)',
      role: 'Authoritative Durable Transactional Relational Database',
      nodes: ['postgresql-authoritative-db (Primary)', 'mnemosyne-clinical-jpa', 'mnemosyne-operations-jpa'],
      metrics: [
        { label: 'Clinical Database', value: 'fhir_node_authoritative (FHIR R5 Resources)' },
        { label: 'Operations Database', value: 'ops_node_authoritative (Tasks, Audits, State)' },
        { label: 'JPA Engines', value: 'Dual-Cluster WildFly / Quarkus JPA Servers' }
      ],
      description: 'Persistent, ACID-compliant transactional relational datastore preserving clinical resources and task audit history'
    }
  },
  themis: {
    id: 'themis',
    name: 'Themis',
    englishTitle: 'Security & Policy Enforcement',
    description: 'Default-Deny Policy Evaluation & Role-to-Authority RBAC Engine',
    areaId: 'security-policy',
    areaName: 'Security & Policy'
  },
  agora: {
    id: 'agora',
    name: 'Agora',
    englishTitle: 'Collaboration & Matrix Gateway',
    description: 'Matrix/Synapse Collaboration & Healthcare Application Service Bridge',
    areaId: 'collaboration',
    areaName: 'Collaboration',
    middleware: {
      name: 'Synapse Matrix Homeserver & Healthcare AS Bridge',
      technology: 'Matrix Protocol / Synapse 1.100',
      ports: '8008 (Matrix Homeserver), 8095 (Healthcare AS Bridge)',
      role: 'Federated End-to-End Encrypted Healthcare Collaboration',
      nodes: ['agora-synapse-homeserver', 'agora-service-as-bridge'],
      metrics: [
        { label: 'Protocol Bridge', value: 'Agora Application Service (:8095)' },
        { label: 'Homeserver API', value: 'Matrix Client-Server API v3 (:8008)' },
        { label: 'Security Boundary', value: 'Themis Default-Deny Room Governance (Zero-PHI Metadata)' }
      ],
      description: 'Matrix-powered collaboration gateway linking clinical workflows to real-time chat spaces'
    }
  },
  iris: {
    id: 'iris',
    name: 'Iris',
    englishTitle: 'Presentation Services',
    description: 'Presentation Tier & BEFE Dual-Port Gateway',
    areaId: 'presentation',
    areaName: 'Presentation',
    children: [
      { id: 'iris-befe', name: 'Iris BEFE Gateway', englishTitle: 'Dual-Port REST Gateway', description: 'WildFly 31 Jakarta EE gateway (:8080 Clinical, :8090 Operations)' },
      { id: 'iris-clinical', name: 'Iris Clinical SPA', englishTitle: 'Clinical Record Explorer', description: 'Vue 3 Clinical FHIR R5 web application on port 3000' },
      { id: 'iris-monitor', name: 'Iris Monitor SPA', englishTitle: 'Authoritative Operations Console', description: 'Vue 3 Operational telemetry console on port 3001' },
      { id: 'iris-administration', name: 'Iris Administration SPA', englishTitle: 'Provider Registry Portal', description: 'Vue 3 Self-service and registry workbench on port 3002' }
    ]
  }
};

/**
 * Resolves an authoritative subsystem descriptor for a given ID,
 * searching top-level subsystems as well as nested child components.
 */
export function findAuthoritativeSubsystem(id: string): AuthoritativeSubsystem | null {
  if (!id) return null;
  const normalizedId = id.toLowerCase().trim();
  
  if (AUTHORITATIVE_SUBSYSTEMS[normalizedId]) {
    return AUTHORITATIVE_SUBSYSTEMS[normalizedId];
  }
  
  for (const parent of Object.values(AUTHORITATIVE_SUBSYSTEMS)) {
    if (parent.children) {
      const child = parent.children.find(c => c.id.toLowerCase() === normalizedId);
      if (child) {
        return {
          id: child.id,
          name: child.name,
          englishTitle: child.englishTitle,
          description: child.description,
          areaId: parent.areaId,
          areaName: parent.areaName,
          middleware: parent.middleware
        };
      }
    }
  }
  
  return null;
}
