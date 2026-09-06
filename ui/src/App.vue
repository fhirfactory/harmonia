<script setup lang="ts">
import Navbar from './components/Navbar.vue';
</script>

<template>
  <div class="min-h-screen flex flex-col bg-[#0b0f19] text-slate-100">
    <Navbar />
    <main class="flex-1 container py-6">
      <router-view />
    </main>
    <footer class="border-t border-[#27344d] py-4 text-center text-xs text-slate-500">
      <div class="container">
        HIE 5-Tier FHIR Platform &copy; 2026. Clustered In-Memory Grid (Infinispan 15) &bull; BEFE (WildFly Jakarta EE 10) &bull; HAPI FHIR JPA (FHIR R5)
      </div>
    </footer>
  </div>
</template>
