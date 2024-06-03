import { writable } from 'svelte/store';

const defaultOpt: userData = {
	token: null,
	username: null
};

export type userData = { token: string | null; username: string | null };

export const userStore = writable(defaultOpt);
