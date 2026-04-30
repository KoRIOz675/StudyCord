import axios from "axios";

const BASE_URL = "/api";

export const getServers = () => axios.get(`${BASE_URL}/servers`);
export const getChannels = (serverId) =>
  axios.get(`${BASE_URL}/channels/server/${serverId}`);
export const getMessages = (channelId) =>
  axios.get(`${BASE_URL}/messages/channel/${channelId}`);
export const postMessage = (channelId, content, authorId) =>
  axios.post(`${BASE_URL}/messages/${channelId}`, { content, authorId });
export const getSuggestions = (userId) =>
  axios.get(`${BASE_URL}/algorithms/suggest-servers/${userId}`);
export const getSimilarMessages = (channelId, query) =>
  axios.get(`${BASE_URL}/algorithms/similar-messages/${channelId}`, {
    params: { query },
  });
export const getUsers = () => axios.get(`${BASE_URL}/users`);
export const getServersByUser = (userId) =>
  axios.get(`${BASE_URL}/users/${userId}/servers`);
