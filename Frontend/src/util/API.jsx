export async function registerAccount(data) {
  return fetch(`http://142.93.148.156:80/u/signin/register?`, {
    method: "POST",
    body: data,
  })
    .then(response => {
      return response.json();
    })
    .catch(error => {
      return;
    });
}

export async function validateLogin(email, password) {
  return fetch(`http://142.93.148.156:80/u/signin/auth/validate?email=${email}&password=${password}`, {
    method: "GET",
  })
    .then(response => {
      if (response.ok) {
        return response.json(); // Returns promise
      }
      return Promise.reject(response); // Not ok then reject promise --
    })
    .then(data => {
      return data; // Return data (userID and username) if all is good
    })
    .catch(error => {
      return error; // Catch rejected promise and send back error --
    });
}

export async function getLoginToken(userID) {
  return fetch(`http://142.93.148.156:80/u/signin/auth/get/token?&userID=${userID}`, {
    method: "GET",
  }).then(response => {
    if (response.ok) {
      return response.json(); // Returns promise
    }
    return Promise.reject(response);
  });
}

export async function createWorkspace(userID, workspaceName, isPublic) {
  return fetch(`http://142.93.148.156:80/u/assets/w/create?userID=${userID}&workspaceName=${workspaceName}&isPublic=${isPublic}`, {
    method: "POST",
  }).then(response => {
    return response.json();
  });
}

export async function createChart(workspaceID, chartName) {
  return fetch(`http://142.93.148.156:80/u/assets/c/create?workspaceID=${workspaceID}&chartName=${chartName}`, {
    method: "POST",
  }).then(response => {
    return response.json();
  });
}

export async function createItem(chartID, itemName, description) {
  return fetch(`http://142.93.148.156:80/u/assets/i/create?chartID=${chartID}&itemName=${itemName}&description=${description}`, {
    method: "POST",
  }).then(response => {
    return response.json();
  });
}

export async function validateToken(userID, token, expiryDate) {
  return fetch(`http://142.93.148.156:80/u/signin/auth/check/token?userID=${userID}&accessToken=${token}&expiryDateString=${expiryDate}`, {
    method: "GET",
  }).then(response => {
    return response.json();
  });
}

export async function validateCurrentToken() {
  return fetch(`http://142.93.148.156:80/u/signin/auth/check/token?userID=${getCookie("userID")}&accessToken=${getCookie("token")}&expiryDateString=${getCookie("tokenExpiry")}`, {
    method: "GET",
  }).then(response => {
    return response.json();
  });
}

export async function nullifyToken(userID) {
  return fetch(`http://142.93.148.156:80/u/signin/nullify/token?userID=${userID}`, {
    method: "POST",
  }).then(response => {
    return response.json();
  });
}

export async function getAllUserWorkspaces(userID) {
  return fetch(`http://142.93.148.156:80/u/get/workspaces/all/userid?userID=${userID}`, {
    method: "GET",
  }).then(response => {
    return response.json();
  });
}

// export async function getWorkspaceIDS(userID) {
//   return fetch(`http://142.93.148.156:80/u/get/workspaces/all/userid?userID=${userID}`, {
//     method: "GET",
//   }).then(response => {
//     return response.json();
//   });
// }

// export async function getWorkspaceCreationDate(workspaceID) {
//   return fetch(`http://142.93.148.156:80/u/get/workspaces/createddate/workspaceid?workspaceID=${workspaceID}`, {
//     method: "GET",
//   }).then(response => {
//     return response.json();
//   });
// }

// export async function getWorkspaceIsPublic(workspaceID) {
//   return fetch(`http://142.93.148.156:80/u/get/workspaces/ispublic/workspaceid?workspaceID=${workspaceID}`, {
//     method: "GET",
//   }).then(response => {
//     return response.json();
//   });
// }

export async function getWorkspacesDisplay(userID) {
  return fetch(`http://142.93.148.156:80/u/get/workspaces/display/userid?userID=${userID}`, {
    method: "GET",
  }).then(response => {
    return response.json();
  });
}

export async function getWorkspaceData(workspaceID) {
  return fetch(`http://142.93.148.156:80/u/w/${workspaceID}`, {
    method: "GET",
  }).then(response => {
    return response.json();
  });
}

export async function authenticateWorkspace(userID, workspaceID) {
  try {
    return await fetch(`http://142.93.148.156:80/u/w/auth/${userID}/${workspaceID}`, {
      method: "GET",
    })
      .then(response => {
        return response.json();
      })
      .then(data => {
        return data.status == "success" ? 1 : -1;
      });
  } catch (error) {
    return 0; // Server offline
  }
}

export async function getWorkspaceName(workspaceID) {
  return fetch(`http://142.93.148.156:80/u/get/workspaces/name/workspaceid?workspaceID=${workspaceID}`, {
    method: "GET",
  }).then(response => {
    return response.json();
  });
}

export async function getWorkspaceCharts(workspaceID) {
  return fetch(`http://142.93.148.156:80/u/get/charts/workspaceid?workspaceID=${workspaceID}`, {
    method: "GET",
  }).then(response => {
    return response.json();
  });
}

export async function getChartsDisplay(workspaceID) {
  return fetch(`http://142.93.148.156:80/u/get/charts/display/workspaceid?workspaceID=${workspaceID}`, {
    method: "GET",
  }).then(response => {
    return response.json();
  });
}

export async function setItemName(itemID, newName) {
  return fetch(`http://142.93.148.156:80/u/set/item/name/itemID?itemID=${itemID}&name=${newName}`, {
    method: "GET",
  }).then(response => {
    return response.json();
  });
}

export async function setItemDescription(itemID, description) {
  return fetch(`http://142.93.148.156:80/u/set/item/description/itemID?itemID=${itemID}&description=${description}`, {
    method: "POST",
  }).then(response => {
    return response.json();
  });
}

export async function deleteItem(itemID) {
  return fetch(`http://142.93.148.156:80/u/delete/item/itemid?itemID=${itemID}`, {
    method: "POST",
  }).then(response => {
    return response.json();
  });
}

export async function deleteChart(chartID) {
  return fetch(`http://142.93.148.156:80/u/delete/chart/chartid?chartID=${chartID}`, {
    method: "POST",
  }).then(response => {
    return response.json();
  });
}

export async function changeAccountUsername(data) {

    return fetch(`http://142.93.148.156:80/u/set/user/name/userid`, {
      method: "POST",
      body: data
    }).then(response => {
      return response.json();
    }).catch(error => {
      return {status: "offline"};
    });

}

export async function changeAccountEmail(data) {
  return fetch(`http://142.93.148.156:80/u/set/user/email/userid`, {
    method: "POST",
    body: data,
  }).then(response => {
    return response.json();
  }).catch(error => {
    return {status: "offline"};
  });
}

export async function changeAccountPassword(data) {
  return fetch(`http://142.93.148.156:80/u/set/user/password/userid`, {
    method: "POST",
    body: data,
  }).then(response => {
    return response.json();
  });
}
