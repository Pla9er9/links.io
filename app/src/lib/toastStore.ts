import { writable } from "svelte/store";

export type toast = {
    title: string | null
    text: string
    variant: "danger" | null 
}

const defaultOpts: toast = {
    title: null,
    text: "",
    variant: null,
}

export const toastStore = writable<toast[]>([])
