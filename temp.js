

const x = 1000000 ; //10 Lakhs



async function callAwait(i){
    
    return new Promise((resolve, reject) => {
        const timer = setTimeout(() => {
            resolve(i);
        }, 10 * 1000 )
        });
    
}

async function call() {
    
    let output = [] ;
    for(let i = 0 ; i < x ; i++ ){
         output.push(callAwait(i));
    }
    const result = await Promise.all(output);
    console.log('called..',result);
    console.log('completed..')
}

call();
