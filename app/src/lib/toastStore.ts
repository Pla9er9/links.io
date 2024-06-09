import { writable } from "svelte/store";

export type toast = {
    title: string | null
    text: string
    variant: "danger" | "succes" | null 
}

export const toastStore = writable<toast[]>([])
