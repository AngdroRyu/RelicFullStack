<script setup lang="ts">
import { ref, onMounted } from "vue";
import api from "../services/api";
import type { User } from "../types/User";

// Reactive state
const users = ref<User[]>([]);
const name = ref("");
const email = ref("");
const editUserId = ref<number | null>(null);
const editName = ref("");
const editEmail = ref("");

// Fetch all users
const fetchUsers = async () => {
	const res = await api.get<User[]>("/users");
	users.value = res.data;
};

// Add a new user
const addUser = async () => {
	if (!name.value || !email.value) return;
	await api.post<User>("/users", {
		name: name.value,
		email: email.value
	});
	name.value = "";
	email.value = "";
	fetchUsers();
};

// Delete a user
const deleteUser = async (id: number) => {
	await api.delete(`/users/${id}`);
	fetchUsers();
};

// Start editing a user
const startEdit = (user: User) => {
	editUserId.value = user.id;
	editName.value = user.name;
	editEmail.value = user.email;
};

// Cancel editing
const cancelEdit = () => {
	editUserId.value = null;
	editName.value = "";
	editEmail.value = "";
};

// Save edited user
const saveEdit = async () => {
	if (editUserId.value === null) return;
	await api.put<User>(`/users/${editUserId.value}`, {
		name: editName.value,
		email: editEmail.value
	});
	cancelEdit();
	fetchUsers();
};

// Load users on mount
onMounted(fetchUsers);
</script>

<template>
	<div>
		<h1>Users</h1>

		<!-- Add user -->
		<input v-model="name" placeholder="Name" />
		<input v-model="email" placeholder="Email" />
		<button @click="addUser">Add User</button>

		<!-- Users list -->
		<ul>
			<li v-for="u in users" :key="u.id">
				<div v-if="editUserId !== u.id">
					{{ u.name }} - {{ u.email }}
					<button @click="startEdit(u)">Edit</button>
					<button @click="deleteUser(u.id)">Delete</button>
				</div>
				<div v-else>
					<input v-model="editName" />
					<input v-model="editEmail" />
					<button @click="saveEdit">Save</button>
					<button @click="cancelEdit">Cancel</button>
				</div>
			</li>
		</ul>
	</div>
</template>
