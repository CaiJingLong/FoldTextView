#FolodTextView
##主要功能，可以折叠的TextView

####核心调用代码
    tv = (FoldTextView) findViewById(R.id.tv);
    tv.setDuration(200);
    //        tv.setMax(2000);//不设置则自动wrap_content，设置则max根据设置
    tv.setMin(0);
    tv.setText("hello world\n\ndsljfl\ndsfjds");

    tv.setOnFoldChangeListener(new FoldTextView.OnFoldChangeListener() {
    @Override
    public void onFoldEnd(FoldTextView textView, boolean isFold) {
    Log.d("MainActivity", "isFold:" + isFold);
    }

    });
