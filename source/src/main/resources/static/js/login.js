"use strict";
document
  .querySelector("#loginForm")
  .addEventListener("submit", async (event) => {
    event.preventDefault();
    const form = event.target,
      button = form.querySelector("[type=submit]"),
      error = document.querySelector("#loginError");
    button.disabled = true;
    error.textContent = "";
    try {
      const response = await fetch("/auth/api/login", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(Object.fromEntries(new FormData(form))),
      });
      const data = await response.json();
      if (!response.ok)
        throw new Error(data.message || "Không đăng nhập được.");
      form.reset();
      location.assign("/app");
    } catch (e) {
      error.textContent = e.message;
    } finally {
      button.disabled = false;
    }
  });
