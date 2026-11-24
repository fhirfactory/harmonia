// Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later
// DOM-contract smoke test; this does not claim a visual browser inspection.
const fs = require('fs'), vm = require('vm'), assert = require('assert');
const html = fs.readFileSync(process.argv[2], 'utf8');
const data = html.match(/<script type="application\/json" id="architecture">([\s\S]*?)<\/script>/)[1];
const code = [...html.matchAll(/<script>([\s\S]*?)<\/script>/g)].at(-1)[1];
const rows = JSON.parse(data);
class Element {
  constructor() { this.children = []; this.value = ''; this.listeners = {}; this.innerHTML = ''; }
  append(x) { this.children.push(x); }
  replaceChildren() { this.children = []; }
  addEventListener(name, fn) { this.listeners[name] = fn; }
  querySelectorAll() { return []; }
}
const nodes = Object.fromEntries(['architecture','search','type','state','list','total','detail'].map(x => [x,new Element()]));
nodes.architecture.textContent = data;
const location = {hash:''}, window = {addEventListener() {}};
vm.runInNewContext(code,{document:{getElementById:id=>nodes[id],createElement:()=>new Element()},location,window});
assert.equal(nodes.list.children.length, rows.length);
nodes.search.value=rows[0].id;nodes.search.listeners.input();assert.equal(nodes.list.children.length,1);
nodes.list.children[0].children[0].onclick();assert.equal(location.hash,rows[0].id);assert(nodes.detail.innerHTML.includes('Canonical Markdown'));
nodes.search.value='';nodes.type.value='Capability / Feature';nodes.type.listeners.input();assert.equal(nodes.list.children.length,179);
nodes.state.value='current';nodes.state.listeners.input();assert.equal(nodes.list.children.length,rows.filter(r=>r.type==='Capability / Feature'&&r.currentness==='current').length);
console.log(JSON.stringify({status:'PASS',elements:rows.length,checks:['initial population','identity search','selection','Feature filter','combined currentness filter'],limits:'DOM-contract smoke test; no pixel/visual browser claim'}));
