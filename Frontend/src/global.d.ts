export {};

declare global {
  interface Window {
    REACT_APP_API_BASE_URL?: string;
  }
}